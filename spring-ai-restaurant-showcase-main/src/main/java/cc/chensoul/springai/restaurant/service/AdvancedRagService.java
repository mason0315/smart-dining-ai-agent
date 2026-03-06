package cc.chensoul.springai.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 高级 RAG 服务
 * 提供 Re-ranking、混合搜索等高级 RAG 功能
 */
@Slf4j
@Service
public class AdvancedRagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public AdvancedRagService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    /**
     * 带 Re-ranking 的 RAG 搜索
     * 先进行向量搜索，然后使用 LLM 对结果进行重新排序
     *
     * @param query 查询文本
     * @param topK  初始检索数量
     * @param topN  最终返回数量
     * @return 重新排序后的文档
     */
    public List<Document> searchWithReranking(String query, int topK, int topN) {
        log.info("执行 Re-ranking RAG 搜索: query={}, topK={}, topN={}", query, topK, topN);

        // 第一步：向量相似性搜索（获取更多候选结果）
        List<Document> candidates = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .build()
        );

        if (candidates.isEmpty()) {
            return new ArrayList<>();
        }

        // 第二步：使用 LLM 对结果进行重新排序
        StringBuilder candidatesText = new StringBuilder();
        for (int i = 0; i < candidates.size(); i++) {
            candidatesText.append(i + 1)
                    .append(". ")
                    .append(candidates.get(i).getFormattedContent())
                    .append("\n");
        }

        String rerankPrompt = String.format("""
                根据以下查询，对以下文档进行相关性排序：
                
                查询: %s
                
                文档列表:
                %s
                
                请返回最相关的 %d 个文档的序号（用逗号分隔），按相关性从高到低排序。
                只返回数字，不要有其他文字。
                """, query, candidatesText, topN);

        String rerankResult = chatClient.prompt()
                .user(rerankPrompt)
                .call()
                .content();

        // 解析排序结果
        List<Integer> indices = parseIndices(rerankResult, topN);
        List<Document> reranked = new ArrayList<>();
        for (Integer index : indices) {
            if (index > 0 && index <= candidates.size()) {
                reranked.add(candidates.get(index - 1));
            }
        }

        log.info("Re-ranking 完成，返回 {} 个结果", reranked.size());
        return reranked;
    }

    /**
     * 混合搜索（向量搜索 + 关键词搜索）
     * 结合语义相似度和关键词匹配
     *
     * @param query     查询文本
     * @param topK      返回结果数量
     * @param keywordWeight 关键词权重（0.0-1.0）
     * @return 混合搜索结果
     */
    public List<Document> hybridSearch(String query, int topK, double keywordWeight) {
        log.info("执行混合搜索: query={}, topK={}, keywordWeight={}", query, topK, keywordWeight);

        // 向量搜索
        List<Document> vectorResults = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK * 2) // 获取更多候选
                        .build()
        );

        // 关键词搜索（简单实现：基于内容包含关键词）
        List<Document> keywordResults = vectorResults.stream()
                .filter(doc -> containsKeywords(doc.getFormattedContent(), extractKeywords(query)))
                .collect(Collectors.toList());

        // 合并和评分
        Map<String, Document> docMap = vectorResults.stream()
                .collect(Collectors.toMap(
                        doc -> doc.getId(),
                        doc -> doc,
                        (d1, d2) -> d1
                ));

        // 计算综合得分
        List<ScoredDocument> scoredDocs = docMap.values().stream()
                .map(doc -> {
                    double vectorScore = calculateVectorScore(doc, vectorResults);
                    double keywordScore = keywordResults.contains(doc) ? 1.0 : 0.0;
                    double combinedScore = (1 - keywordWeight) * vectorScore + keywordWeight * keywordScore;
                    return new ScoredDocument(doc, combinedScore);
                })
                .sorted(Comparator.comparing(ScoredDocument::score).reversed())
                .limit(topK)
                .collect(Collectors.toList());

        List<Document> results = scoredDocs.stream()
                .map(ScoredDocument::document)
                .collect(Collectors.toList());

        log.info("混合搜索完成，返回 {} 个结果", results.size());
        return results;
    }

    /**
     * 多查询 RAG
     * 生成多个相关查询，然后合并结果
     *
     * @param query 原始查询
     * @param topK  每个查询返回的结果数量
     * @return 合并后的结果
     */
    public List<Document> multiQueryRag(String query, int topK) {
        log.info("执行多查询 RAG: query={}, topK={}", query, topK);

        // 生成多个相关查询
        String queryGenerationPrompt = String.format("""
                为以下查询生成3个相关的查询问题，用于更好地检索信息：
                原始查询: %s
                
                请返回3个查询，每行一个，不要编号。
                """, query);

        String queriesText = chatClient.prompt()
                .user(queryGenerationPrompt)
                .call()
                .content();

        String[] queries = queriesText.split("\n");
        List<String> queryList = new ArrayList<>();
        queryList.add(query); // 包含原始查询
        for (String q : queries) {
            String trimmed = q.trim();
            if (!trimmed.isEmpty() && trimmed.length() > 5) {
                queryList.add(trimmed);
            }
        }

        // 对每个查询进行搜索
        Map<String, Document> resultMap = new java.util.HashMap<>();
        for (String q : queryList) {
            List<Document> docs = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(q)
                            .topK(topK)
                            .build()
            );
            for (Document doc : docs) {
                resultMap.put(doc.getId(), doc);
            }
        }

        List<Document> results = new ArrayList<>(resultMap.values());
        log.info("多查询 RAG 完成，返回 {} 个结果", results.size());
        return results;
    }

    /**
     * 使用高级 RAG 进行聊天
     *
     * @param query 用户查询
     * @return AI 回答
     */
    public String chatWithAdvancedRag(String query) {
        log.info("高级 RAG 聊天: {}", query);

        // 使用 Re-ranking 搜索
        List<Document> documents = searchWithReranking(query, 10, 5);

        // 构建上下文
        StringBuilder context = new StringBuilder();
        for (Document doc : documents) {
            context.append(doc.getFormattedContent()).append("\n\n");
        }

        String response = chatClient.prompt()
                .user("基于以下上下文回答问题：\n\n" + context + "\n\n问题：" + query)
                .call()
                .content();

        return response;
    }

    // 辅助方法

    private List<Integer> parseIndices(String result, int maxCount) {
        List<Integer> indices = new ArrayList<>();
        String[] parts = result.trim().split("[,\\s]+");
        for (String part : parts) {
            try {
                int index = Integer.parseInt(part.trim());
                if (index > 0 && indices.size() < maxCount) {
                    indices.add(index);
                }
            } catch (NumberFormatException e) {
                // 忽略无效的数字
            }
        }
        return indices;
    }

    private boolean containsKeywords(String content, List<String> keywords) {
        String lowerContent = content.toLowerCase();
        return keywords.stream()
                .anyMatch(keyword -> lowerContent.contains(keyword.toLowerCase()));
    }

    private List<String> extractKeywords(String query) {
        // 简单的关键词提取（可以改进）
        return List.of(query.split("\\s+"));
    }

    private double calculateVectorScore(Document doc, List<Document> results) {
        int index = results.indexOf(doc);
        if (index == -1) {
            return 0.0;
        }
        // 简单的评分：位置越靠前，分数越高
        return 1.0 - (index * 0.1);
    }

    private record ScoredDocument(Document document, double score) {
    }
}
