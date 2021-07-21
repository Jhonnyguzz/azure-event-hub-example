package com.azure.eventhub.controller;

import com.azure.eventhub.service.Producer;
import com.azure.eventhub.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Slf4j
public class PublisherController {

    private Producer producer;

    @Autowired
    public PublisherController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/send")
    public String sendFileToEventHub(@RequestBody String message) {
        String[] splittedMessage = message.split("::");
        String base64Message = Utils.getBase64StringFile(splittedMessage[0]);
        producer.send(Utils.splitStringInto250kb(base64Message), splittedMessage[1]);
        return message;
    }
}
