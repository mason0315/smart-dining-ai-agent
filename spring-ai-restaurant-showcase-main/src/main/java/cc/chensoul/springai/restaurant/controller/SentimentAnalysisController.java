package cc.chensoul.springai.restaurant.controller;

import cc.chensoul.springai.restaurant.service.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 情感分析控制器
 * 提供情感分析相关的 API 端点
 */
@Slf4j
@RestController
@RequestMapping("/api/sentiment")
@RequiredArgsConstructor
public class SentimentAnalysisController {

    private final SentimentAnalysisService sentimentAnalysisService;

    /**
     * 分析单条评论的情感
     *
     * @param request 包含评论内容的请求
     * @return 情感分析结果
     */
    @PostMapping("/analyze")
    public ResponseEntity<SentimentAnalysisService.SentimentResult> analyzeSentiment(
            @RequestBody Map<String, String> request) {
        String review = request.get("review");
        if (review == null || review.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("情感分析请求: {}", review);
            SentimentAnalysisService.SentimentResult result =
                    sentimentAnalysisService.analyzeSentiment(review);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("情感分析失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 批量分析评论情感
     *
     * @param request 包含评论列表的请求
     * @return 情感分析摘要
     */
    @PostMapping("/batch-analyze")
    public ResponseEntity<SentimentAnalysisService.SentimentSummary> batchAnalyzeSentiment(
            @RequestBody Map<String, List<String>> request) {
        List<String> reviews = request.get("reviews");
        if (reviews == null || reviews.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("批量情感分析请求，共 {} 条评论", reviews.size());
            SentimentAnalysisService.SentimentSummary summary =
                    sentimentAnalysisService.batchAnalyzeSentiment(reviews);
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            log.error("批量情感分析失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
