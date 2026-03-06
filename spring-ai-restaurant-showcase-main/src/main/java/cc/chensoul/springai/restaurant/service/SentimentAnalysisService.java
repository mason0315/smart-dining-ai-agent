package cc.chensoul.springai.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 情感分析服务
 * 分析用户评论和反馈的情感倾向
 */
@Slf4j
@Service
public class SentimentAnalysisService {

    private final ChatClient chatClient;

    public SentimentAnalysisService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 分析评论情感
     *
     * @param review 用户评论
     * @return 情感分析结果
     */
    public SentimentResult analyzeSentiment(String review) {
        log.info("分析评论情感: {}", review);

        PromptTemplate template = new PromptTemplate("""
                请分析以下餐厅评论的情感倾向，返回JSON格式结果：
                评论内容: {review}
                
                返回格式：
                {{
                  "sentiment": "positive/negative/neutral",
                  "score": 0.0-1.0,
                  "emotions": ["高兴", "失望", "满意", "愤怒"],
                  "summary": "情感摘要"
                }}
                """);

        Prompt prompt = template.create(Map.of("review", review));

        SentimentResult result = chatClient.prompt(prompt)
                .call()
                .entity(SentimentResult.class);

        log.info("情感分析完成: {}", result);
        return result;
    }

    /**
     * 批量分析评论情感
     *
     * @param reviews 评论列表
     * @return 情感分析摘要（包含整体情感倾向、统计信息和关键洞察）
     */
    public SentimentSummary batchAnalyzeSentiment(java.util.List<String> reviews) {
        log.info("批量分析评论情感，共 {} 条评论", reviews.size());

        StringBuilder reviewsText = new StringBuilder();
        for (int i = 0; i < reviews.size(); i++) {
            reviewsText.append(i + 1).append(". ").append(reviews.get(i)).append("\n");
        }

        PromptTemplate template = new PromptTemplate("""
                请分析以下餐厅评论的整体情感倾向：
                {reviews}
                
                返回JSON格式结果：
                {{
                  "overallSentiment": "positive/negative/neutral",
                  "averageScore": 0.0-1.0,
                  "positiveCount": 0,
                  "negativeCount": 0,
                  "neutralCount": 0,
                  "keyInsights": ["关键洞察1", "关键洞察2"]
                }}
                """);

        Prompt prompt = template.create(Map.of("reviews", reviewsText.toString()));

        SentimentSummary summary = chatClient.prompt(prompt)
                .call()
                .entity(SentimentSummary.class);

        log.info("批量情感分析完成: {}", summary);
        return summary;
    }

    /**
     * 情感分析结果
     */
    public record SentimentResult(
            String sentiment,
            Double score,
            java.util.List<String> emotions,
            String summary
    ) {
    }

    /**
     * 情感分析摘要
     */
    public record SentimentSummary(
            String overallSentiment,
            Double averageScore,
            Integer positiveCount,
            Integer negativeCount,
            Integer neutralCount,
            java.util.List<String> keyInsights
    ) {
    }
}
