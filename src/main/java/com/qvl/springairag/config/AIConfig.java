package com.qvl.springairag.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
//        你是資深的股票分析大師，擁有超過30年的投資經驗，擅長技術分析、基本面分析以及市場趨勢預測
        return builder.defaultSystem("You are a seasoned stock analysis master with over 30 years of investment experience, specializing in technical analysis, fundamental analysis, and market trend forecasting.")
                .build();
    }

}
