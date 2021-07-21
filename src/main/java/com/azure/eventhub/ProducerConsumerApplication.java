package com.azure.eventhub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class ProducerConsumerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ProducerConsumerApplication.class, args);
	}

}
