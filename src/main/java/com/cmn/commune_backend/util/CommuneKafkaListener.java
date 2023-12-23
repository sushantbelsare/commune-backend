package com.cmn.commune_backend.util;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CommuneKafkaListener {

    @KafkaListener(topics = "sush", groupId = "sush_group")
    public void listenGroupSush(@Payload String message){
        System.out.println("Received Message in group foo: " + message);
    }
}
