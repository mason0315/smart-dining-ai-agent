package cc.chensoul.springai.restaurant.model;

import cc.chensoul.springai.restaurant.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class DishTest {

    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish();
    }

    @Test
    void testDishGettersAndSetters() {
        // 使用 TestDataFactory 创建测试数据
        Dish testDish = TestDataFactory.createTestDish();

        // 验证 getter 方法
        assertEquals(1L, testDish.getId());
        assertEquals("宫保鸡丁", testDish.getName());
        assertEquals("经典的川菜，麻辣鲜香", testDish.getDescription());
        assertEquals("川菜", testDish.getCuisine());
        assertEquals(68.0, testDish.getPrice());
        assertEquals("主菜", testDish.getCategory());
        assertEquals("高蛋白", testDish.getDietaryInfo());
        assertEquals(450, testDish.getCalories());
        assertEquals("20分钟", testDish.getPreparationTime());
        assertEquals("中等", testDish.getDifficulty());
        assertEquals(Arrays.asList("鸡胸肉", "花生", "干辣椒", "花椒"), testDish.getIngredients());
    }

    @Test
    void testDishFromFactory() {
        // 测试 TestDataFactory 创建的菜品对象
        Dish testDish = TestDataFactory.createTestDish();
        
        assertNotNull(testDish);
        assertEquals("宫保鸡丁", testDish.getName());
        assertEquals("川菜", testDish.getCuisine());
        assertEquals(68.0, testDish.getPrice());
        assertEquals(450, testDish.getCalories());
    }

    @Test
    void testDishWithNullValues() {
        // 测试空值处理
        assertNull(dish.getId());
        assertNull(dish.getName());
        assertNull(dish.getDescription());
        assertNull(dish.getCuisine());
        assertNull(dish.getPrice());
        assertNull(dish.getCategory());
        assertNull(dish.getDietaryInfo());
        assertNull(dish.getCalories());
        assertNull(dish.getPreparationTime());
        assertNull(dish.getDifficulty());
        assertNull(dish.getIngredients());
    }

    @Test
    void testDishWithEmptyIngredients() {
        dish.setIngredients(Arrays.asList());
        assertNotNull(dish.getIngredients());
        assertTrue(dish.getIngredients().isEmpty());
    }

    @Test
    void testDishPriceBoundaries() {
        // 测试价格边界值
        dish.setPrice(0.0);
        assertEquals(0.0, dish.getPrice());

        dish.setPrice(999.99);
        assertEquals(999.99, dish.getPrice());

        dish.setPrice(25.5);
        assertEquals(25.5, dish.getPrice());
    }

    @Test
    void testDishCaloriesBoundaries() {
        // 测试卡路里边界值
        dish.setCalories(0);
        assertEquals(0, dish.getCalories());

        dish.setCalories(2000);
        assertEquals(2000, dish.getCalories());

        dish.setCalories(500);
        assertEquals(500, dish.getCalories());
    }
}
