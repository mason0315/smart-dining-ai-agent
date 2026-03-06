package cc.chensoul.springai.restaurant.controller;

import cc.chensoul.springai.restaurant.service.DocumentService;
import cc.chensoul.springai.restaurant.service.RagChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * RAG 控制器
 * 提供 RAG 相关的 API 端点
 */
@Slf4j
@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {
    private final RagChatService ragChatService;
    private final DocumentService documentService;

    /**
     * 加载文档到向量存储
     *
     * @param request 包含文件路径的请求
     * @return 操作结果
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadDocuments(@RequestBody Map<String, String> request) {
        String filePath = request.get("filePath");
        if (filePath == null || filePath.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "文件路径不能为空"));
        }

        try {
            log.info("开始加载文档: {}", filePath);
            documentService.loadDocuments(filePath);
            return ResponseEntity.ok(Map.of("message", "文档加载成功", "filePath", filePath));
        } catch (Exception e) {
            log.error("加载文档失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "文档加载失败: " + e.getMessage()));
        }
    }

    /**
     * RAG 聊天接口
     *
     * @param message 用户消息
     * @return AI 回答
     */
    @PostMapping("/chat")
    public ResponseEntity<String> chatWithRag(@RequestBody String message) {
        log.info("RAG 聊天请求: {}", message);
        try {
            String response = ragChatService.chatWithRag(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("RAG 聊天失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("处理请求时发生错误: " + e.getMessage());
        }
    }

    /**
     * 向量相似性搜索
     *
     * @param request 包含查询文本和数量的请求
     * @return 检索结果
     */
    @PostMapping("/search")
    public ResponseEntity<List<Document>> search(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        Integer topK = (Integer) request.getOrDefault("topK", 5);

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("执行向量相似性搜索: {}, topK: {}", query, topK);
            List<Document> documents = ragChatService.searchSimilar(query, topK);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("向量相似性搜索失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 个性化 RAG 聊天
     *
     * @param request 包含消息和用户偏好的请求
     * @return AI 回答
     */
    @PostMapping("/chat-personalized")
    public ResponseEntity<String> chatWithPersonalizedRag(@RequestBody Map<String, Object> request) {
        String message = (String) request.get("message");
        @SuppressWarnings("unchecked")
        Map<String, Object> userPreferences = (Map<String, Object>) request.getOrDefault("userPreferences", Map.of());

        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("消息内容不能为空");
        }

        try {
            log.info("个性化 RAG 聊天请求: {}, 偏好: {}", message, userPreferences);
            String response = ragChatService.chatWithPersonalizedRag(message, userPreferences);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("个性化 RAG 聊天失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("处理请求时发生错误: " + e.getMessage());
        }
    }
}

