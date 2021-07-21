package com.azure.eventhub.service.impl;

import com.azure.eventhub.service.Consumer;
import com.azure.eventhub.settings.ApplicationProperties;
import com.azure.eventhub.util.Utils;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConsumerImpl implements Consumer, CommandLineRunner {

    private ApplicationProperties applicationProperties;
    private String filename;
    private List<String> partitionFile;

    @Autowired
    public ConsumerImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void consume() {
        String stringConnection = applicationProperties.getConnectionString();
        EventHubConsumerAsyncClient consumer = new EventHubClientBuilder()
                .connectionString(stringConnection)
                .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                .buildAsyncConsumerClient();

        consumer.receiveFromPartition(applicationProperties.getPartitionId(), EventPosition.latest()).subscribe(e -> {
            String message = e.getData().getBodyAsString();
            log.info("Mensaje recibido: " + message.getBytes().length + " bytes");
            checkMessage(message);
        });
    }

    private void checkMessage(String message) {
        if(message.split("::")[0].equals("Begin")) {
            partitionFile = new ArrayList<>();
            filename = message.split("::")[1];
        } else if(message.equals("End")) {
            log.info("Reconstruyendo archivo");
            Utils.writeFile(partitionFile, filename);
        } else {
            partitionFile.add(message);
        }
    }

    /**
     * Debugging purposes
     */
    @Deprecated
    public void consume2() {
        String stringConnection = applicationProperties.getConnectionString();
        EventHubConsumerAsyncClient consumer = new EventHubClientBuilder()
                .connectionString(stringConnection)
                .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                .buildAsyncConsumerClient();

        consumer.receive(true).subscribe(e ->
                {
                    var message = e.getData().getBodyAsString();
                    log.info("The message consumed is: "+message);
                    System.out.println(message);
                }
        );
    }

    @Override
    public void run(String... args) throws Exception {
        consume();
    }
}
