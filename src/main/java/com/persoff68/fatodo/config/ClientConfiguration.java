package com.persoff68.fatodo.config;

import com.persoff68.fatodo.client.ChatServiceClient;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.client.WsServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final BeanFactory beanFactory;

    @Bean
    @Primary
    public ChatServiceClient chatClient() {
        return (ChatServiceClient) beanFactory.getBean("chatServiceClientWrapper");
    }

    @Bean
    @Primary
    public EventServiceClient eventClient() {
        boolean kafkaProducerExists = beanFactory.containsBean("eventProducer");
        return kafkaProducerExists
                ? (EventServiceClient) beanFactory.getBean("eventProducer")
                : (EventServiceClient) beanFactory.getBean("eventServiceClientWrapper");
    }

    @Bean
    @Primary
    public UserServiceClient userClient() {
        return (UserServiceClient) beanFactory.getBean("userServiceClientWrapper");
    }

    @Bean
    @Primary
    public WsServiceClient wsClient() {
        boolean kafkaProducerExists = beanFactory.containsBean("wsProducer");
        return kafkaProducerExists
                ? (WsServiceClient) beanFactory.getBean("wsProducer")
                : (WsServiceClient) beanFactory.getBean("wsServiceClientWrapper");
    }

}
