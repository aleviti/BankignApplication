package com.example.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.tools.OrderTools;

@Configuration
public class AiConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder , OrderTools orderTools){
        return builder
        .defaultSystem("""  
        
         "You are a helpful order assistant 
         Keep your response shot and clear 
         because they be conversated into spetch
         Use the available tool whenever that user asks about on order status.
         Never invent order information.
        """)
        .defaultAdvisors(new SimpleLoggerAdvisor())
        .defaultTools(orderTools)
        .build();
    }
}
