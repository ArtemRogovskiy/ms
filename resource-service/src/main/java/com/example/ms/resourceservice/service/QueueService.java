package com.example.ms.resourceservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import com.example.ms.resourceservice.configuration.RabbitConfig;

@Service
public class QueueService {

    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    @Autowired
    public QueueService(RabbitTemplate rabbitTemplate, RetryTemplate retryTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.retryTemplate = retryTemplate;
    }

    public void sendResourceMeta(Integer resourceId) {
        retryTemplate.execute(context -> {
            rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, resourceId);
            return null;
        });
    }
}
