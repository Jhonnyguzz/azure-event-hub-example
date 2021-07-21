package com.azure.eventhub.service.impl;

import com.azure.eventhub.service.Producer;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.models.CreateBatchOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.azure.eventhub.settings.ApplicationProperties;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProducerImpl implements Producer {

    private ApplicationProperties applicationProperties;

    @Autowired
    public ProducerImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void send(List<String> fileBase64chunked, String filename) {
        fileBase64chunked.add(0, "Begin::"+filename);
        fileBase64chunked.add("End");
        sendToFirstPartition(fileBase64chunked);
    }

    private void sendToFirstPartition(List<String> events) {
        log.info("Trying to produce to partition");
        String stringConnection = applicationProperties.getConnectionString();

        EventHubProducerClient producer = new EventHubClientBuilder()
                .connectionString(stringConnection)
                .buildProducerClient();

        List<EventData> allEvents = events.stream().map(EventData::new).collect(Collectors.toList());

        CreateBatchOptions options = new CreateBatchOptions().setPartitionId(applicationProperties.getPartitionId());
        EventDataBatch eventDataBatch = producer.createBatch(options);

        for (EventData eventData : allEvents) {
            if (!eventDataBatch.tryAdd(eventData)) {
                producer.send(eventDataBatch);
                eventDataBatch = producer.createBatch(options);
                if (!eventDataBatch.tryAdd(eventData)) {
                    throw new IllegalArgumentException("Event is too large for an empty batch. Max size: "
                            + eventDataBatch.getMaxSizeInBytes());
                }
            }
        }

        if (eventDataBatch.getCount() > 0) {
            producer.send(eventDataBatch);
        }

        producer.close();
    }
}
