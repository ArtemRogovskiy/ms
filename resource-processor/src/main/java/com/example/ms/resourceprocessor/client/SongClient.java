package com.example.ms.resourceprocessor.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SongClient {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final RetryTemplate retryTemplate;

    @Autowired
    public SongClient(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    public HttpResponse<String> sendPostRequest(Map<String, String> meta) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8092/songs"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(meta)))
                .build();

        return retryTemplate.execute(e -> client.send(request, HttpResponse.BodyHandlers.ofString()));
    }
}
