package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Album;
import service.AlbumService;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class AlbumController implements HttpHandler {
    private final AlbumService service = new AlbumService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path =  exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        if (method.equalsIgnoreCase("GET")) {
            if (path.equals("/albums")) {
                listAlbum(exchange);
            } else if (path.startsWith("/albums/")) {
                searchAlbum(exchange, path);
            }
        } else if (method.equalsIgnoreCase("POST")) {
            createAlbum(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            updateAlbum(exchange, path);
        } else if (method.equalsIgnoreCase("DELETE")) {
            deleteAlbum(exchange, path);
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void listAlbum(HttpExchange exchange) throws IOException {
        String response = gson.toJson(service.getList());
        sendResponse(exchange, 200, response);
    }

    private void createAlbum(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Album album = gson.fromJson(isr, Album.class);
        Album created = service.create(album);
        sendResponse(exchange, 201, gson.toJson(created));
    }

    private void searchAlbum(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        Optional<Album> album = service.get(id);
        if (album.isPresent()) {
            sendResponse(exchange, 200, gson.toJson(album.get()));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Album not found\"}");
        }
    }

    private void deleteAlbum(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        boolean deleted = service.delete(id);
        if (deleted) {
            sendResponse(exchange, 204, "{\"message\": \"Album successfully deleted\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Album not found\"}");
        }
    }

    private void updateAlbum(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Album album = gson.fromJson(isr, Album.class);
        album.setId(id);

        boolean updated = service.update(album);
        if (updated) {
            sendResponse(exchange, 200, gson.toJson(album));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Album not found\"}");
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
