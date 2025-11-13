package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Review;
import service.ReviewService;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class ReviewController implements HttpHandler {
    private final ReviewService service = new ReviewService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path =  exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        if (method.equalsIgnoreCase("GET")) {
            if (path.equals("/reviews")) {
                listReview(exchange);
            } else if (path.startsWith("/reviews/")) {
                searchReview(exchange, path);
            }
        } else if (method.equalsIgnoreCase("POST")) {
            createReview(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            updateReview(exchange, path);
        } else if (method.equalsIgnoreCase("DELETE")) {
            deleteReview(exchange, path);
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void listReview(HttpExchange exchange) throws IOException {
        String response = gson.toJson(service.getList());
        sendResponse(exchange, 200, response);
    }

    private void createReview(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Review review = gson.fromJson(isr, Review.class);
        Review created = service.create(review);
        sendResponse(exchange, 201, gson.toJson(created));
    }

    private void searchReview(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        Optional<Review> review = service.get(id);
        if (review.isPresent()) {
            sendResponse(exchange, 200, gson.toJson(review.get()));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Review not found\"}");
        }
    }

    private void deleteReview(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        boolean deleted = service.delete(id);
        if (deleted) {
            sendResponse(exchange, 204, "{\"message\": \"Review successfully deleted\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Review not found\"}");
        }
    }

    private void updateReview(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Review review = gson.fromJson(isr, Review.class);
        review.setId(id);

        boolean updated = service.update(review);
        if (updated) {
            sendResponse(exchange, 200, gson.toJson(review));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Review not found\"}");
        }
    }


    private String extractIdFromUrl(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    private void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
