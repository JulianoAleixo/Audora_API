package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Playlist;
import service.PlaylistService;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class PlaylistController implements HttpHandler {
    private final PlaylistService service = new PlaylistService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path =  exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        if (method.equalsIgnoreCase("GET")) {
            if (path.equals("/playlists")) {
                listPlaylist(exchange);
            } else if (path.startsWith("/playlists/")) {
                searchPlaylist(exchange, path);
            }
        } else if (method.equalsIgnoreCase("POST")) {
            createPlaylist(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            updatePlaylist(exchange, path);
        } else if (method.equalsIgnoreCase("DELETE")) {
            deletePlaylist(exchange, path);
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void listPlaylist(HttpExchange exchange) throws IOException {
        String response = gson.toJson(service.getList());
        sendResponse(exchange, 200, response);
    }

    private void createPlaylist(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Playlist playlist = gson.fromJson(isr, Playlist.class);
        Playlist created = service.create(playlist);
        sendResponse(exchange, 201, gson.toJson(created));
    }

    private void searchPlaylist(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        Optional<Playlist> playlist = service.get(id);
        if (playlist.isPresent()) {
            sendResponse(exchange, 200, gson.toJson(playlist.get()));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Playlist not found\"}");
        }
    }

    private void deletePlaylist(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        boolean deleted = service.delete(id);
        if (deleted) {
            sendResponse(exchange, 204, "{\"message\": \"Playlist successfully deleted\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Playlist not found\"}");
        }
    }

    private void updatePlaylist(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Playlist playlist = gson.fromJson(isr, Playlist.class);
        playlist.setId(id);

        boolean updated = service.update(playlist);
        if (updated) {
            sendResponse(exchange, 200, gson.toJson(playlist));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Playlist not found\"}");
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
