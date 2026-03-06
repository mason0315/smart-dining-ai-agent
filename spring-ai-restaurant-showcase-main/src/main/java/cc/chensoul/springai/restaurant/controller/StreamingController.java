package cc.chensoul.springai.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 流式响应控制器
 * 提供 Server-Sent Events (SSE) 流式输出功能
 */
@Slf4j
@RestController
@RequestMapping("/api/streaming")
@RequiredArgsConstructor
public class StreamingController {

    private final ChatClient chatClient;

    /**
     * 流式聊天接口
     * 使用 SSE 实时返回 AI 响应
     *
     * @param request 包含用户消息的请求
     * @return SSE 流
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            SseEmitter emitter = new SseEmitter();
            CompletableFuture.runAsync(() -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data("消息内容不能为空"));
                    emitter.complete();
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            });
            return emitter;
        }

        SseEmitter emitter = new SseEmitter(60000L); // 60秒超时

        CompletableFuture.runAsync(() -> {
            try {
                log.info("开始流式聊天: {}", message);

                // 使用流式调用 - 在 Spring AI 1.1.2 中，stream().content() 返回 Flux<String>
                Flux<String> contentStream = chatClient.prompt()
                        .user(message)
                        .stream()
                        .content();

                // 订阅 Flux 并发送 SSE 事件
                contentStream.subscribe(
                        chunk -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("message")
                                        .data(chunk));
                            } catch (Exception e) {
                                log.error("发送 SSE 数据失败", e);
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            log.error("流式聊天失败: {}", error.getMessage(), error);
                            emitter.completeWithError(error);
                        },
                        () -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("complete")
                                        .data("流式响应完成"));
                                emitter.complete();
                                log.info("流式聊天完成");
                            } catch (Exception e) {
                                log.error("完成 SSE 流失败", e);
                                emitter.completeWithError(e);
                            }
                        }
                );
            } catch (Exception e) {
                log.error("流式聊天失败: {}", e.getMessage(), e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    /**
     * 流式餐厅推荐
     * 实时返回推荐结果
     *
     * @param request 推荐请求
     * @return SSE 流
     */
    @PostMapping(value = "/recommend", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamRecommendations(@RequestBody Map<String, Object> request) {
        String location = (String) request.getOrDefault("location", "北京市");
        String cuisine = (String) request.getOrDefault("cuisine", "不限");

        SseEmitter emitter = new SseEmitter(60000L);

        CompletableFuture.runAsync(() -> {
            try {
                log.info("开始流式推荐: location={}, cuisine={}", location, cuisine);

                String prompt = String.format(
                        "推荐5家位于%s的%s餐厅，逐个介绍每家餐厅的特色。",
                        location, cuisine
                );

                // 使用流式调用 - 在 Spring AI 1.1.2 中，stream().content() 返回 Flux<String>
                Flux<String> contentStream = chatClient.prompt()
                        .user(prompt)
                        .stream()
                        .content();

                // 订阅 Flux 并发送 SSE 事件
                contentStream.subscribe(
                        chunk -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("recommendation")
                                        .data(chunk));
                            } catch (Exception e) {
                                log.error("发送推荐数据失败", e);
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            log.error("流式推荐失败: {}", error.getMessage(), error);
                            emitter.completeWithError(error);
                        },
                        () -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("complete")
                                        .data("推荐完成"));
                                emitter.complete();
                            } catch (Exception e) {
                                log.error("完成 SSE 流失败", e);
                                emitter.completeWithError(e);
                            }
                        }
                );

            } catch (Exception e) {
                log.error("流式推荐失败: {}", e.getMessage(), e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
