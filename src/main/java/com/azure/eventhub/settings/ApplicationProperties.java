package com.azure.eventhub.settings;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ApplicationProperties {

    @Value("${azure.connection-string}")
    private String connectionString;

    @Value("${azure.partition-id}")
    private String partitionId;
}
