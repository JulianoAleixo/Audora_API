package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Track;
import service.TrackService;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class TrackController implements HttpHandler {
    private final TrackService service = new TrackService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path =  exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        if (method.equalsIgnoreCase("GET")) {
            if (path.equals("/tracks")) {
                listTrack(exchange);
            } else if (path.startsWith("/tracks/")) {
                searchTrack(exchange, path);
            }
        } else if (method.equalsIgnoreCase("POST")) {
            createTrack(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            updateTrack(exchange, path);
        } else if (method.equalsIgnoreCase("DELETE")) {
            deleteTrack(exchange, path);
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void listTrack(HttpExchange exchange) throws IOException {
        String response = gson.toJson(service.getList());
        sendResponse(exchange, 200, response);
    }

    private void createTrack(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Track track = gson.fromJson(isr, Track.class);
        Track created = service.create(track);
        sendResponse(exchange, 201, gson.toJson(created));
    }

    private void searchTrack(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        Optional<Track> track = service.get(id);
        if (track.isPresent()) {
            sendResponse(exchange, 200, gson.toJson(track.get()));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Track not found\"}");
        }
    }

    private void deleteTrack(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        boolean deleted = service.delete(id);
        if (deleted) {
            sendResponse(exchange, 204, "{\"message\": \"Track successfully deleted\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Track not found\"}");
        }
    }

    private void updateTrack(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Track track = gson.fromJson(isr, Track.class);
        track.setId(id);

        boolean updated = service.update(track);
        if (updated) {
            sendResponse(exchange, 200, gson.toJson(track));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Track not found\"}");
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
