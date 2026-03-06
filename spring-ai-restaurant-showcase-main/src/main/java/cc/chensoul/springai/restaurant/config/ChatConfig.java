package cc.chensoul.springai.restaurant.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chat 相关配置
 * 配置 ChatClient 和日志记录
 */
@Slf4j
@Configuration
public class ChatConfig {

    /**
     * 配置聊天记忆
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    /**
     * 配置 ChatClient
     * 包含记忆功能和日志记录
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, VectorStore vectorStore) {
        log.info("初始化 ChatClient 配置，包含记忆功能和日志记录");
        return chatClientBuilder
                .defaultSystem("你是一个专业的餐厅推荐助手。请用中文回答，提供准确、有用的餐厅和菜品推荐。")
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder()
                                .similarityThreshold(0.7).topK(5).build()).build(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor())
                .build();
    }
}
