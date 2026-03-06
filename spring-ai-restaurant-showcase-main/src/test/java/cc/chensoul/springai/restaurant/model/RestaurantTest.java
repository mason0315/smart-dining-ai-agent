package cc.chensoul.springai.restaurant.model;

import cc.chensoul.springai.restaurant.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
    }

    @Test
    void testRestaurantGettersAndSetters() {
        // 使用 TestDataFactory 创建测试数据
        Restaurant testRestaurant = TestDataFactory.createTestRestaurant();

        // 验证 getter 方法
        assertEquals(1L, testRestaurant.getId());
        assertEquals("测试餐厅", testRestaurant.getName());
        assertEquals("中餐", testRestaurant.getCuisine());
        assertEquals("北京市", testRestaurant.getLocation());
        assertEquals(4.5, testRestaurant.getRating());
        assertEquals("这是一家测试餐厅", testRestaurant.getDescription());
        assertEquals("中等价位", testRestaurant.getPriceRange());
        assertArrayEquals(new String[]{"环境优雅", "服务周到"}, testRestaurant.getFeatures());
    }

    @Test
    void testRestaurantFromFactory() {
        // 测试 TestDataFactory 创建的餐厅对象
        Restaurant testRestaurant = TestDataFactory.createTestRestaurant();
        
        assertNotNull(testRestaurant);
        assertEquals("测试餐厅", testRestaurant.getName());
        assertEquals("中餐", testRestaurant.getCuisine());
        assertEquals(4.5, testRestaurant.getRating());
    }

    @Test
    void testRestaurantWithNullValues() {
        // 测试空值处理
        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
        assertNull(restaurant.getCuisine());
        assertNull(restaurant.getLocation());
        assertNull(restaurant.getRating());
        assertNull(restaurant.getDescription());
        assertNull(restaurant.getPriceRange());
        assertNull(restaurant.getFeatures());
    }

    @Test
    void testRestaurantWithEmptyFeatures() {
        restaurant.setFeatures(new String[]{});
        assertNotNull(restaurant.getFeatures());
        assertEquals(0, restaurant.getFeatures().length);
    }

    @Test
    void testRestaurantRatingBoundaries() {
        // 测试评分边界值
        restaurant.setRating(0.0);
        assertEquals(0.0, restaurant.getRating());

        restaurant.setRating(5.0);
        assertEquals(5.0, restaurant.getRating());

        restaurant.setRating(2.5);
        assertEquals(2.5, restaurant.getRating());
    }
}
