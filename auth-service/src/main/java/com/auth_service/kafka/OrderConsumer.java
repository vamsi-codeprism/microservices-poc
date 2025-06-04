package com.auth_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_service.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String message){
        try {
            OrderEvent event = objectMapper.readValue(message, OrderEvent.class);
            LOGGER.info("Order event received in auth service => {}", event.toString());
            System.out.println("Order event received in auth service => " + event);

            // Process your order event here

        } catch (Exception e) {
            LOGGER.error("Error deserializing message: {}", message, e);
        }
    }

}