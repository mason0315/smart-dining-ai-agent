package cc.chensoul.springai.restaurant.model;

import cc.chensoul.springai.restaurant.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class RecommendationRequestTest {

    private RecommendationRequest request;

    @BeforeEach
    void setUp() {
        request = new RecommendationRequest();
    }

    @Test
    void testRecommendationRequestGettersAndSetters() {
        // 使用 TestDataFactory 创建测试数据
        RecommendationRequest testRequest = TestDataFactory.createTestRecommendationRequest();

        // 验证 getter 方法
        assertEquals("北京市", testRequest.getLocation());
        assertEquals("中餐", testRequest.getCuisine());
        assertEquals("中等价位", testRequest.getPriceRange());
        assertEquals("商务聚餐", testRequest.getOccasion());
        assertEquals(4, testRequest.getGroupSize());
        assertEquals("晚餐", testRequest.getTimeOfDay());
        assertEquals(Arrays.asList("素食"), testRequest.getDietaryRestrictions());
        assertEquals(Arrays.asList("环境优雅", "适合聊天"), testRequest.getPreferences());
    }

    @Test
    void testRecommendationRequestFromFactory() {
        // 测试 TestDataFactory 创建的推荐请求对象
        RecommendationRequest testRequest = TestDataFactory.createTestRecommendationRequest();
        
        assertNotNull(testRequest);
        assertEquals("北京市", testRequest.getLocation());
        assertEquals("中餐", testRequest.getCuisine());
        assertEquals(4, testRequest.getGroupSize());
    }

    @Test
    void testEmptyRecommendationRequestFromFactory() {
        // 测试空的推荐请求对象
        RecommendationRequest emptyRequest = TestDataFactory.createEmptyRecommendationRequest();
        
        assertNotNull(emptyRequest);
        assertNull(emptyRequest.getLocation());
        assertNull(emptyRequest.getCuisine());
        assertNull(emptyRequest.getGroupSize());
    }

    @Test
    void testRecommendationRequestForSpecificCuisine() {
        // 测试特定菜系的推荐请求
        RecommendationRequest chineseRequest = TestDataFactory.createRecommendationRequestForCuisine("中餐");
        RecommendationRequest westernRequest = TestDataFactory.createRecommendationRequestForCuisine("西餐");
        
        assertEquals("中餐", chineseRequest.getCuisine());
        assertEquals("西餐", westernRequest.getCuisine());
        assertEquals(2, chineseRequest.getGroupSize());
        assertEquals(2, westernRequest.getGroupSize());
    }

    @Test
    void testRecommendationRequestWithNullValues() {
        // 测试空值处理
        assertNull(request.getLocation());
        assertNull(request.getCuisine());
        assertNull(request.getPriceRange());
        assertNull(request.getOccasion());
        assertNull(request.getGroupSize());
        assertNull(request.getTimeOfDay());
        assertNull(request.getDietaryRestrictions());
        assertNull(request.getPreferences());
    }

    @Test
    void testRecommendationRequestWithEmptyLists() {
        // 测试空列表
        request.setDietaryRestrictions(Arrays.asList());
        request.setPreferences(Arrays.asList());

        assertNotNull(request.getDietaryRestrictions());
        assertNotNull(request.getPreferences());
        assertTrue(request.getDietaryRestrictions().isEmpty());
        assertTrue(request.getPreferences().isEmpty());
    }

    @Test
    void testRecommendationRequestGroupSizeBoundaries() {
        // 测试人数边界值
        request.setGroupSize(1);
        assertEquals(1, request.getGroupSize());

        request.setGroupSize(20);
        assertEquals(20, request.getGroupSize());

        request.setGroupSize(8);
        assertEquals(8, request.getGroupSize());
    }

    @Test
    void testRecommendationRequestWithSingleItemLists() {
        // 测试单元素列表
        request.setDietaryRestrictions(Arrays.asList("素食"));
        request.setPreferences(Arrays.asList("环境优雅"));

        assertEquals(1, request.getDietaryRestrictions().size());
        assertEquals(1, request.getPreferences().size());
        assertEquals("素食", request.getDietaryRestrictions().get(0));
        assertEquals("环境优雅", request.getPreferences().get(0));
    }
}
