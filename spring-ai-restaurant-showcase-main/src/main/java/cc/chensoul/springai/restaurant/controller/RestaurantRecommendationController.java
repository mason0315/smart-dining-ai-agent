package cc.chensoul.springai.restaurant.controller;

import cc.chensoul.springai.restaurant.model.Dish;
import cc.chensoul.springai.restaurant.model.RecommendationRequest;
import cc.chensoul.springai.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantRecommendationController {
    private final ChatClient chatClient;

    /**
     * 根据用户偏好推荐餐厅
     */
    @PostMapping("/recommend")
    public ResponseEntity<List<Restaurant>> recommendRestaurants(@RequestBody RecommendationRequest request) {
        PromptTemplate template = new PromptTemplate("""
                根据以下用户偏好推荐5家合适的餐厅：
                位置: {location}
                菜系: {cuisine}
                价格范围: {priceRange}
                饮食限制: {dietaryRestrictions}
                场合: {occasion}
                人数: {groupSize}
                用餐时间: {timeOfDay}
                其他偏好: {preferences}
                
                请返回餐厅列表，包含餐厅名称、菜系、位置、评分、描述、价格范围和特色，按照评分从高到低排序。
                不要包含任何解释性文字，只返回JSON格式的餐厅数据。
                """);

        Prompt prompt = template.create(Map.of(
                "location", request.getLocation() != null ? request.getLocation() : "北京市",
                "cuisine", request.getCuisine() != null ? request.getCuisine() : "不限",
                "priceRange", request.getPriceRange() != null ? request.getPriceRange() : "不限",
                "dietaryRestrictions", request.getDietaryRestrictions() != null ?
                        String.join(", ", request.getDietaryRestrictions()) : "无",
                "occasion", request.getOccasion() != null ? request.getOccasion() : "日常用餐",
                "groupSize", request.getGroupSize() != null ? request.getGroupSize().toString() : "1-2人",
                "timeOfDay", request.getTimeOfDay() != null ? request.getTimeOfDay() : "午餐",
                "preferences", request.getPreferences() != null ?
                        String.join(", ", request.getPreferences()) : "无"
        ));

        List<Restaurant> restaurants = chatClient.prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<Restaurant>>() {
                });

        return ResponseEntity.ok(restaurants);
    }

    /**
     * 生成菜品描述和营养信息（支持中文参数）
     * 使用 POST 方法避免 URL 编码问题
     */
    @PostMapping("/dishes/generate")
    public ResponseEntity<List<Dish>> generateDishes(@RequestBody Map<String, Object> request) {
        String cuisine = (String) request.get("cuisine");
        Integer countObj = (Integer) request.get("count");
        int count = countObj != null ? countObj : 5;

        // 限制菜品数量在合理范围内
        int validCount = Math.max(1, Math.min(count, 10));

        PromptTemplate template = new PromptTemplate("""
                为{cuisine}菜系生成{count}道特色菜品，包含以下信息：
                - 菜品名称
                - 详细描述
                - 主要食材
                - 价格范围
                - 菜品分类（开胃菜/主菜/甜点等）
                - 饮食信息（素食/无麸质/低卡等）
                - 卡路里
                - 制作时间
                - 难度等级
                
                请返回JSON格式的菜品数据，不要包含解释性文字。
                """);

        Prompt prompt = template.create(Map.of(
                "cuisine", cuisine,
                "count", validCount
        ));

        List<Dish> dishes = chatClient.prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<Dish>>() {
                });

        return ResponseEntity.ok(dishes);
    }

    /**
     * 获取个性化用餐建议（POST 方法，避免 URL 编码问题）
     */
    @PostMapping("/advice")
    public ResponseEntity<String> getDiningAdvice(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");

        PromptTemplate template = new PromptTemplate("""
                作为专业的餐厅推荐顾问，请回答以下问题：{query}
                
                请提供实用、具体的建议，包括：
                1. 餐厅选择建议
                2. 菜品推荐
                3. 用餐时间建议
                4. 注意事项
                
                回答要简洁明了，不超过200字。
                """);

        Prompt prompt = template.create(Map.of(
                "query", query
        ));

        String advice = chatClient.prompt(prompt)
                .call()
                .content();

        return ResponseEntity.ok(advice);
    }

    /**
     * 多语言交互 - 支持中文和英文（POST 方法，避免 URL 编码问题）
     */
    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody Map<String, Object> request) {
        String message = (String) request.get("message");
        String language = (String) request.getOrDefault("language", "zh");

        String systemPrompt = language.equals("en") ?
                "You are a helpful restaurant recommendation assistant. Answer in English." :
                "你是一个专业的餐厅推荐助手。请用中文回答。";

        PromptTemplate template = new PromptTemplate("""
                {systemPrompt}
                
                用户问题: {message}
                
                请根据用户的问题提供相关的餐厅推荐或用餐建议。
                """);

        Prompt prompt = template.create(Map.of(
                "systemPrompt", systemPrompt,
                "message", message
        ));

        String response = chatClient.prompt(prompt)
                .call()
                .content();

        return ResponseEntity.ok(response);
    }

    /**
     * 获取餐厅详细信息
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Restaurant> getRestaurantDetails(@PathVariable Long id) {
        PromptTemplate template = new PromptTemplate("""
                请为ID为{id}的餐厅生成详细信息，包括：
                - 餐厅名称
                - 菜系类型
                - 具体位置
                - 评分（1-5星）
                - 详细描述
                - 价格范围
                - 特色服务（如：外卖、堂食、包间等）
                
                返回JSON格式的餐厅数据。
                """);

        Prompt prompt = template.create(Map.of("id", id));

        Restaurant restaurant = chatClient.prompt(prompt)
                .call()
                .entity(Restaurant.class);

        return ResponseEntity.ok(restaurant);
    }
}
