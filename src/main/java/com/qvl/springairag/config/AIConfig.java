package com.qvl.springairag.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI的設置：如角色...等等
 */
@Configuration
public class AIConfig {

    private final ChatMemory chatMemory = new InMemoryChatMemory();

    private final VectorStore vectorStore;

    public AIConfig(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
//        你是資深的股票分析大師，擁有超過70年的投資經驗，擅長技術分析、基本面分析以及市場趨勢預測
        return builder
                .defaultSystem("""
                        You are a seasoned stock analysis master with over 70 years of investment experience,
                         specializing in technical analysis, fundamental analysis, and market trend forecasting.
                        """)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore),
                        new SimpleLoggerAdvisor())
                .build();
    }

}
