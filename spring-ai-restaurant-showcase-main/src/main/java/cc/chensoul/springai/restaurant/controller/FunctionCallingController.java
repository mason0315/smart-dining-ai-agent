package cc.chensoul.springai.restaurant.controller;

import cc.chensoul.springai.restaurant.service.FunctionCallingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Function Calling 控制器
 * 提供 Function Calling 相关的 API 端点
 */
@Slf4j
@RestController
@RequestMapping("/api/function-calling")
@RequiredArgsConstructor
public class FunctionCallingController {

    private final FunctionCallingService functionCallingService;

    /**
     * Function Calling 聊天接口（提示词增强版本）
     * 
     * 注意：当前实现使用提示词增强方式模拟 Function Calling，
     * AI 会根据提示词提供相关信息，但不会真正调用外部函数。
     * 如需完整的 Function Calling 功能，需要模型支持 Tool Calling。
     *
     * @param request 包含用户消息的请求
     * @return AI 回答（包含基于提示词的相关信息）
     */
    @PostMapping("/chat")
    public ResponseEntity<String> chatWithFunctions(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("消息内容不能为空");
        }

        try {
            log.info("Function Calling 聊天请求: {}", message);
            String response = functionCallingService.chatWithFunctions(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Function Calling 聊天失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("处理请求时发生错误: " + e.getMessage());
        }
    }
}
