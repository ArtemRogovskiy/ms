package com.example.ms.resourceprocessor.client;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResourceClient {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final RetryTemplate retryTemplate;

    @Autowired
    public ResourceClient(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    public Map<String, String> receiveResourceMeta(Integer resourceId) throws Exception {
        HttpHeaders responseHeaders = sendGetRequest(resourceId).headers();
        Optional<String> meta = responseHeaders.firstValue("Meta-Data");
        TypeReference<HashMap<String, String>> typeRef
                = new TypeReference<>() {
        };
        return mapper.readValue(meta.orElse(""), typeRef);
    }

    private HttpResponse<InputStream> sendGetRequest(Integer resourceId) throws Exception {
        HttpRequest requestResource = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8090/resources/" + resourceId))
                .GET()
                .build();

        return retryTemplate.execute(arg -> client.send(requestResource, HttpResponse.BodyHandlers.ofInputStream()));
    }
}
