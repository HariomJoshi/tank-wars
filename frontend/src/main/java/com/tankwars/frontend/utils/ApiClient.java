package com.tankwars.frontend.utils;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private static final Gson gson = new Gson();
    HttpClient client = HttpClient.newHttpClient();
    // This function takes a URL and a HashMap as input and sends the data of the HashMap to the URL as a POST request
    public CompletableFuture<Boolean> sendPostRequest(String url, HashMap<String, String> data) {
        // Convert HashMap to JSON string
        String jsonData = gson.toJson(data);
        // Create the HTTP client and request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonData))
                .build();
        // Send the request asynchronously and handle the response
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    System.out.println("Response: " + response.body());
                    int statusCode = response.statusCode();
                    // Check for success status codes (e.g., 200 or 201)
                    return (statusCode == 200 || statusCode == 201);
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return false;
                });
    }

    public CompletableFuture<Boolean> sendPostReqQuery(String query) {
        // Construct the URL with query parameters



        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query)) // Use the full URL with query parameters
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json") // Set the correct content type
                .POST(BodyPublishers.ofString("")) // Empty body for POST
                .build();

        // Send the request asynchronously and handle the response
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    System.out.println("Response: " + response.body());
                    System.out.println(response.statusCode());
                    return (response.statusCode() == 200 || response.statusCode() == 201); // Return true if status code is 200 (OK)
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return false; // Return false in case of an error
                });
    }


}
