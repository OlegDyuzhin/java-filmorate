package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BaseControllerTest {

    protected HttpResponse<String> httpResponse;

    @BeforeEach
    void setUp() throws IOException {
        FilmorateApplication.run(new String[]{""});
    }

    @AfterEach
    void tearDown() {
        FilmorateApplication.stop();
    }

    final HttpResponse<String> getResponse(String path, RequestMethod requestMethod, String jsonData) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        URI uri = URI.create("http://localhost:8080" + path);

        switch (requestMethod) {
            case GET -> builder.GET();
            case POST -> builder.POST(HttpRequest.BodyPublishers.ofString(jsonData));
            case PUT -> builder.PUT(HttpRequest.BodyPublishers.ofString(jsonData));
        }

        HttpRequest httpRequest = builder.uri(uri).version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json;charset=utf-8").build();

        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}