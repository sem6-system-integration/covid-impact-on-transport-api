package com.pollub.covidimpactontransportapi.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MyHttpClient {
    public static HttpResponse<String> get(String url) throws IOException, InterruptedException {
        System.out.println("GET: " + url);
        var uri = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(uri)
                .GET()
                .header("accept", "application/json")
                .timeout(Duration.ofSeconds(30))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
