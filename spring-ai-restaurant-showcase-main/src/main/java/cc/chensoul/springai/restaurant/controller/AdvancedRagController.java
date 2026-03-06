package cc.chensoul.springai.restaurant.controller;

import cc.chensoul.springai.restaurant.service.AdvancedRagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 高级 RAG 控制器
 * 提供 Re-ranking、混合搜索等高级 RAG 功能的 API 端点
 */
@Slf4j
@RestController
@RequestMapping("/api/advanced-rag")
@RequiredArgsConstructor
public class AdvancedRagController {

    private final AdvancedRagService advancedRagService;

    /**
     * Re-ranking RAG 搜索
     *
     * @param request 包含查询和参数的请求
     * @return 重新排序后的文档
     */
    @PostMapping("/rerank")
    public ResponseEntity<List<Document>> rerankSearch(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        Integer topK = (Integer) request.getOrDefault("topK", 10);
        Integer topN = (Integer) request.getOrDefault("topN", 5);

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("Re-ranking 搜索请求: query={}, topK={}, topN={}", query, topK, topN);
            List<Document> documents = advancedRagService.searchWithReranking(query, topK, topN);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Re-ranking 搜索失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 混合搜索（向量 + 关键词）
     *
     * @param request 包含查询和参数的请求
     * @return 混合搜索结果
     */
    @PostMapping("/hybrid-search")
    public ResponseEntity<List<Document>> hybridSearch(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        Integer topK = (Integer) request.getOrDefault("topK", 5);
        Double keywordWeight = request.containsKey("keywordWeight") ?
                ((Number) request.get("keywordWeight")).doubleValue() : 0.3;

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("混合搜索请求: query={}, topK={}, keywordWeight={}", query, topK, keywordWeight);
            List<Document> documents = advancedRagService.hybridSearch(query, topK, keywordWeight);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("混合搜索失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 多查询 RAG
     *
     * @param request 包含查询的请求
     * @return 合并后的搜索结果
     */
    @PostMapping("/multi-query")
    public ResponseEntity<List<Document>> multiQuerySearch(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        Integer topK = (Integer) request.getOrDefault("topK", 5);

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("多查询 RAG 请求: query={}, topK={}", query, topK);
            List<Document> documents = advancedRagService.multiQueryRag(query, topK);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("多查询 RAG 失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 高级 RAG 聊天
     *
     * @param request 包含查询的请求
     * @return AI 回答
     */
    @PostMapping("/chat")
    public ResponseEntity<String> chatWithAdvancedRag(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("查询内容不能为空");
        }

        try {
            log.info("高级 RAG 聊天请求: {}", query);
            String response = advancedRagService.chatWithAdvancedRag(query);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("高级 RAG 聊天失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("处理请求时发生错误: " + e.getMessage());
        }
    }
}
