package com.example.ms.resourceprocessor.rabbitmq;

import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ms.resourceprocessor.client.ResourceClient;
import com.example.ms.resourceprocessor.client.SongClient;

@Component
public class ResourceReceiver {

    Logger logger = LoggerFactory.getLogger(ResourceReceiver.class);

    private final ResourceClient resourceClient;
    private final SongClient songClient;

    @Autowired
    public ResourceReceiver(ResourceClient resourceClient, SongClient songClient) {
        this.resourceClient = resourceClient;
        this.songClient = songClient;
    }


    public void receiveMessage(Integer resourceId) throws Exception {
        logger.info("Resource Processor receive asynchronously resource Id from queue. ResourceId = " + resourceId);

        Map<String, String> meta = resourceClient.receiveResourceMeta(resourceId);
        meta.put("resourceId", resourceId.toString());
        logger.info("Resource Processor receive synchronously meta data from Resource Service. Metadata = " + meta);

        HttpResponse<String> response = songClient.sendPostRequest(meta);
        logger.info("Resource Processor send synchronously post request to Song Service and receive response with statusCode = " + response.statusCode());
    }
}
