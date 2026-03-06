package cc.chensoul.springai.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Function Calling 服务
 * 演示如何使用 Spring AI 的 Function Calling 功能
 * 让 AI 可以调用外部工具和函数来获取实时数据
 * 
 * 注意：在 Spring AI 1.1.2 中，Function Calling/Tool Calling 功能
 * 需要特定的模型支持（如 GPT-4, Claude）和正确的配置。
 * 如果当前模型不支持，将降级到普通聊天模式。
 */
@Slf4j
@Service
public class FunctionCallingService {

    private final ChatClient.Builder chatClientBuilder;
    private final ChatClient chatClient;

    public FunctionCallingService(ChatClient.Builder chatClientBuilder, ChatClient chatClient) {
        this.chatClientBuilder = chatClientBuilder;
        this.chatClient = chatClient;
    }

    /**
     * 获取天气信息的函数（示例）
     */
    public Function<WeatherRequest, WeatherResponse> getWeatherFunction() {
        return request -> {
            log.info("调用天气函数: {}", request);
            // 这里可以调用真实的天气 API
            return new WeatherResponse(
                    request.location(),
                    "晴天",
                    25.0,
                    "适合户外用餐"
            );
        };
    }

    /**
     * 获取餐厅营业时间的函数
     */
    public Function<RestaurantHoursRequest, RestaurantHoursResponse> getRestaurantHoursFunction() {
        return request -> {
            log.info("查询餐厅营业时间: {}", request.restaurantName());
            // 这里可以查询真实的数据库或 API
            return new RestaurantHoursResponse(
                    request.restaurantName(),
                    "09:00-22:00",
                    true
            );
        };
    }

    /**
     * 计算距离的函数
     */
    public Function<DistanceRequest, DistanceResponse> calculateDistanceFunction() {
        return request -> {
            log.info("计算距离: {} -> {}", request.from(), request.to());
            // 这里可以调用地图 API 计算真实距离
            double distance = Math.random() * 10 + 1; // 模拟距离 1-11 公里
            return new DistanceResponse(
                    request.from(),
                    request.to(),
                    distance,
                    (int) (distance * 2) // 估算时间（分钟）
            );
        };
    }

    /**
     * 使用提示词增强方式模拟 Function Calling 进行智能对话
     * 
     * 注意：当前实现为简化版本，使用提示词增强而非真正的 Function Calling。
     * AI 会根据提示词提供相关信息（如天气、营业时间、距离等），但不会真正调用外部函数。
     * 
     * 在 Spring AI 1.1.2 中，完整的 Function Calling/Tool Calling 功能需要：
     * 1. 模型支持 Tool Calling（如 GPT-4, Claude）
     * 2. 正确配置 Tool Callbacks（参考 Spring AI 文档）
     * 3. 使用 FunctionToolCallback 或 MethodToolCallback 注册工具
     * 
     * 如果模型不支持 Tool Calling，当前实现会自动降级到提示词增强模式。
     * 
     * @param userMessage 用户消息
     * @return AI 回答（包含基于提示词的相关信息）
     */
    public String chatWithFunctions(String userMessage) {
        log.info("Function Calling 聊天: {}", userMessage);
        log.warn("注意：Function Calling 需要模型支持 Tool Calling。当前使用普通聊天模式。");

        // 当前实现：使用普通聊天模式
        // 在实际项目中，如果模型支持 Tool Calling，应该使用以下方式：
        // 1. 创建 FunctionToolCallback 实例
        // 2. 使用 chatClientBuilder.defaultTools(...) 注册工具
        // 3. 使用配置了工具的 ChatClient 进行对话
        
        // 这里我们使用提示词来模拟函数调用的效果
        String enhancedPrompt = String.format("""
                用户问题: %s
                
                在回答时，如果需要以下信息，请基于常识提供：
                - 天气信息：如果用户询问天气，提供一般性建议
                - 餐厅营业时间：通常为 09:00-22:00
                - 距离计算：基于地理位置的一般估算
                
                请提供有用的回答。
                """, userMessage);

        String response = chatClient.prompt()
                .user(enhancedPrompt)
                .call()
                .content();

        return response;
    }

    /**
     * 天气请求记录
     */
    public record WeatherRequest(String location) {
    }

    /**
     * 天气响应记录
     */
    public record WeatherResponse(String location, String condition, Double temperature, String suggestion) {
    }

    /**
     * 餐厅营业时间请求记录
     */
    public record RestaurantHoursRequest(String restaurantName) {
    }

    /**
     * 餐厅营业时间响应记录
     */
    public record RestaurantHoursResponse(String restaurantName, String hours, Boolean isOpen) {
    }

    /**
     * 距离计算请求记录
     */
    public record DistanceRequest(String from, String to) {
    }

    /**
     * 距离计算响应记录
     */
    public record DistanceResponse(String from, String to, Double distanceKm, Integer estimatedMinutes) {
    }
}
