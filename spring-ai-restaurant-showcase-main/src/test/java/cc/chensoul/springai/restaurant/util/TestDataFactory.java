package cc.chensoul.springai.restaurant.util;

import cc.chensoul.springai.restaurant.model.Restaurant;
import cc.chensoul.springai.restaurant.model.Dish;
import cc.chensoul.springai.restaurant.model.RecommendationRequest;

import java.util.Arrays;
import java.util.List;

/**
 * 测试数据工厂类
 * 用于创建测试中使用的模拟数据
 */
public class TestDataFactory {

    /**
     * 创建测试用的餐厅对象
     */
    public static Restaurant createTestRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("测试餐厅");
        restaurant.setCuisine("中餐");
        restaurant.setLocation("北京市");
        restaurant.setRating(4.5);
        restaurant.setDescription("这是一家测试餐厅");
        restaurant.setPriceRange("中等价位");
        restaurant.setFeatures(new String[]{"环境优雅", "服务周到"});
        return restaurant;
    }

    /**
     * 创建测试用的餐厅列表
     */
    public static List<Restaurant> createTestRestaurantList() {
        Restaurant restaurant1 = createTestRestaurant();
        
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("测试餐厅2");
        restaurant2.setCuisine("西餐");
        restaurant2.setLocation("上海市");
        restaurant2.setRating(4.2);
        restaurant2.setDescription("这是一家西式餐厅");
        restaurant2.setPriceRange("高端消费");
        restaurant2.setFeatures(new String[]{"浪漫", "精致"});

        return Arrays.asList(restaurant1, restaurant2);
    }

    /**
     * 创建测试用的菜品对象
     */
    public static Dish createTestDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("宫保鸡丁");
        dish.setDescription("经典的川菜，麻辣鲜香");
        dish.setCuisine("川菜");
        dish.setPrice(68.0);
        dish.setCategory("主菜");
        dish.setIngredients(Arrays.asList("鸡胸肉", "花生", "干辣椒", "花椒"));
        dish.setDietaryInfo("高蛋白");
        dish.setCalories(450);
        dish.setPreparationTime("20分钟");
        dish.setDifficulty("中等");
        return dish;
    }

    /**
     * 创建测试用的菜品列表
     */
    public static List<Dish> createTestDishList() {
        Dish dish1 = createTestDish();
        
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("麻婆豆腐");
        dish2.setDescription("另一道经典川菜");
        dish2.setCuisine("川菜");
        dish2.setPrice(48.0);
        dish2.setCategory("主菜");
        dish2.setIngredients(Arrays.asList("豆腐", "牛肉末", "豆瓣酱"));
        dish2.setDietaryInfo("素食可选");
        dish2.setCalories(380);
        dish2.setPreparationTime("15分钟");
        dish2.setDifficulty("简单");

        return Arrays.asList(dish1, dish2);
    }

    /**
     * 创建测试用的推荐请求对象
     */
    public static RecommendationRequest createTestRecommendationRequest() {
        RecommendationRequest request = new RecommendationRequest();
        request.setLocation("北京市");
        request.setCuisine("中餐");
        request.setPriceRange("中等价位");
        request.setDietaryRestrictions(Arrays.asList("素食"));
        request.setOccasion("商务聚餐");
        request.setGroupSize(4);
        request.setTimeOfDay("晚餐");
        request.setPreferences(Arrays.asList("环境优雅", "适合聊天"));
        return request;
    }

    /**
     * 创建空的推荐请求对象
     */
    public static RecommendationRequest createEmptyRecommendationRequest() {
        return new RecommendationRequest();
    }

    /**
     * 创建特定菜系的推荐请求
     */
    public static RecommendationRequest createRecommendationRequestForCuisine(String cuisine) {
        RecommendationRequest request = new RecommendationRequest();
        request.setLocation("北京市");
        request.setCuisine(cuisine);
        request.setPriceRange("中等价位");
        request.setGroupSize(2);
        return request;
    }
}
