package com.azure.eventhub.service;

import java.util.List;

public interface Producer {

    void send(List<String> fileBase64chunked, String filename);

}
