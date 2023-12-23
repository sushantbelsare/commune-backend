package com.cmn.commune_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api_version}")
public class TestController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @PostMapping("topic/message")
    public ResponseEntity<String> addTopicMessage(@RequestParam String message){

        kafkaTemplate.send("sush", message);

        return ResponseEntity.status(HttpStatus.OK).body("Message published");
    }

}
