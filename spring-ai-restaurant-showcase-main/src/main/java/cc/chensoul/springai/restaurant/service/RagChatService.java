package cc.chensoul.springai.restaurant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * RAG 增强的聊天服务
 * 提供基于检索增强生成的智能对话功能
 */
@Service
public class RagChatService {

    private static final Logger log = LoggerFactory.getLogger(RagChatService.class);

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagChatService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    /**
     * 基于 RAG 的聊天对话
     * 使用 Spring AI QuestionAnswerAdvisor 实现
     *
     * @param userMessage 用户消息
     * @return AI 回答
     */
    public String chatWithRag(String userMessage) {
        log.info("开始 RAG 聊天处理: {}", userMessage);

        try {
            // 使用 Advisor 进行 RAG 聊天
            String response = chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();

            log.info("RAG 聊天处理完成");
            return response;

        } catch (Exception e) {
            log.error("RAG 聊天处理失败: {}", e.getMessage(), e);
            throw new RuntimeException("RAG 聊天处理失败: " + e.getMessage(), e);
        }
    }

    /**
     * 向量相似性搜索
     * 基于语义相似度检索相关文档
     *
     * @param query 查询文本
     * @param topK  返回结果数量
     * @return 检索结果
     */
    public List<Document> searchSimilar(String query, int topK) {
        log.info("执行向量相似性搜索: {}, topK: {}", query, topK);

        try {
            List<Document> results = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(query)
                            .topK(topK)
                            .build()
            );

            log.info("向量相似性搜索完成，返回 {} 个结果", results.size());
            return results;

        } catch (Exception e) {
            log.error("向量相似性搜索失败: {}", e.getMessage(), e);
            throw new RuntimeException("向量相似性搜索失败: " + e.getMessage(), e);
        }
    }

    /**
     * 个性化 RAG 聊天
     * 基于用户偏好和历史记录的个性化推荐
     *
     * @param userMessage     用户消息
     * @param userPreferences 用户偏好
     * @return AI 回答
     */
    public String chatWithPersonalizedRag(String userMessage, Map<String, Object> userPreferences) {
        log.info("开始个性化 RAG 聊天: {}, 偏好: {}", userMessage, userPreferences);

        try {
            // 构建个性化过滤条件
            StringBuilder filterExpression = new StringBuilder();
            if (userPreferences.containsKey("city")) {
                filterExpression.append("content LIKE '%").append(userPreferences.get("city")).append("%'");
            }
            if (userPreferences.containsKey("priceRange")) {
                if (filterExpression.length() > 0) {
                    filterExpression.append(" AND ");
                }
                filterExpression.append("content LIKE '%").append(userPreferences.get("priceRange")).append("%'");
            }

            // 使用个性化过滤器
            String response = chatClient.prompt()
                    .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, filterExpression.toString()))
                    .user(userMessage)
                    .call()
                    .content();

            log.info("个性化 RAG 聊天处理完成");
            return response;

        } catch (Exception e) {
            log.error("个性化 RAG 聊天处理失败: {}", e.getMessage(), e);
            throw new RuntimeException("个性化 RAG 聊天处理失败: " + e.getMessage(), e);
        }
    }
}