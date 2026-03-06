package cc.chensoul.springai.restaurant.controller;

import cc.chensoul.springai.restaurant.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 结构化输出示例控制器
 * 展示 Spring AI 的多种结构化输出方式
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/structured")
public class StructuredOutputController {
    
    private final ChatClient chatClient;

    /**
     * 方式1：使用 ParameterizedTypeReference（当前项目使用的方式）
     */
    @PostMapping("/restaurants/type-ref")
    public ResponseEntity<List<Restaurant>> getRestaurantsWithTypeRef(@RequestBody Map<String, Object> request) {
        String cuisine = (String) request.get("cuisine");
        
        PromptTemplate template = new PromptTemplate("""
                推荐3家{cuisine}餐厅，返回JSON格式的餐厅列表。
                每个餐厅包含：name, cuisine, location, rating, description, priceRange, features
                """);
        
        Prompt prompt = template.create(Map.of("cuisine", cuisine));
        
        // 使用 ParameterizedTypeReference
        List<Restaurant> restaurants = chatClient.prompt(prompt)
                .call()
                .entity(new org.springframework.core.ParameterizedTypeReference<List<Restaurant>>() {});
        
        return ResponseEntity.ok(restaurants);
    }

    /**
     * 方式2：使用 BeanOutputConverter
     */
    @PostMapping("/restaurant/bean-converter")
    public ResponseEntity<Restaurant> getRestaurantWithBeanConverter(@RequestBody Map<String, Object> request) {
        String cuisine = (String) request.get("cuisine");
        
        PromptTemplate template = new PromptTemplate("""
                推荐1家{cuisine}餐厅，返回JSON格式的餐厅信息。
                包含：name, cuisine, location, rating, description, priceRange, features
                """);
        
        Prompt prompt = template.create(Map.of("cuisine", cuisine));
        
        // 使用 BeanOutputConverter
        BeanOutputConverter<Restaurant> converter = new BeanOutputConverter<>(Restaurant.class);
        
        String response = chatClient.prompt(prompt)
                .call()
                .content();
        
        Restaurant restaurant = converter.convert(response);
        
        return ResponseEntity.ok(restaurant);
    }

    /**
     * 方式3：使用 ListOutputConverter
     */
    @PostMapping("/restaurants/list-converter")
    public ResponseEntity<List<Map<String, String>>> getRestaurantsWithListConverter(@RequestBody Map<String, Object> request) {
        String cuisine = (String) request.get("cuisine");
        
        PromptTemplate template = new PromptTemplate("""
                推荐3家{cuisine}餐厅，返回JSON格式的餐厅列表。
                每个餐厅包含：name, cuisine, location, rating, description, priceRange, features
                """);
        
        Prompt prompt = template.create(Map.of("cuisine", cuisine));
        
        // 使用 ListOutputConverter
        ListOutputConverter converter = new ListOutputConverter();
        
        String response = chatClient.prompt(prompt)
                .call()
                .content();
        
        @SuppressWarnings("unchecked")
        List<String> restaurantStrings = (List<String>) converter.convert(response);
        
        // 将字符串列表转换为 Map 列表（这里简化处理）
        List<Map<String, String>> restaurants = restaurantStrings.stream()
                .map(str -> Map.of("name", str, "cuisine", cuisine))
                .toList();
        
        return ResponseEntity.ok(restaurants);
    }

    /**
     * 方式4：使用 MapOutputConverter
     */
    @PostMapping("/restaurant/map-converter")
    public ResponseEntity<Map<String, Object>> getRestaurantWithMapConverter(@RequestBody Map<String, Object> request) {
        String cuisine = (String) request.get("cuisine");
        
        PromptTemplate template = new PromptTemplate("""
                推荐1家{cuisine}餐厅，返回JSON格式的餐厅信息。
                包含：name, cuisine, location, rating, description, priceRange, features
                """);
        
        Prompt prompt = template.create(Map.of("cuisine", cuisine));
        
        // 使用 MapOutputConverter
        MapOutputConverter converter = new MapOutputConverter();
        
        String response = chatClient.prompt(prompt)
                .call()
                .content();
        
        @SuppressWarnings("unchecked")
        Map<String, Object> restaurant = (Map<String, Object>) converter.convert(response);
        
        return ResponseEntity.ok(restaurant);
    }

    /**
     * 方式5：直接使用 .entity() 方法（最简单的方式）
     */
    @PostMapping("/restaurant/direct-entity")
    public ResponseEntity<Restaurant> getRestaurantDirectEntity(@RequestBody Map<String, Object> request) {
        String cuisine = (String) request.get("cuisine");
        
        PromptTemplate template = new PromptTemplate("""
                推荐1家{cuisine}餐厅，返回JSON格式的餐厅信息。
                包含：name, cuisine, location, rating, description, priceRange, features
                """);
        
        Prompt prompt = template.create(Map.of("cuisine", cuisine));
        
        // 直接使用 .entity() 方法
        Restaurant restaurant = chatClient.prompt(prompt)
                .call()
                .entity(Restaurant.class);
        
        return ResponseEntity.ok(restaurant);
    }
}
