package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Artist;
import service.ArtistService;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class ArtistController implements HttpHandler {
    private final ArtistService service = new ArtistService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path =  exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        if (method.equalsIgnoreCase("GET")) {
            if (path.equals("/artists")) {
                listArtist(exchange);
            } else if (path.startsWith("/artists/")) {
                searchArtist(exchange, path);
            }
        } else if (method.equalsIgnoreCase("POST")) {
            createArtist(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            updateArtist(exchange, path);
        } else if (method.equalsIgnoreCase("DELETE")) {
            deleteArtist(exchange, path);
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void listArtist(HttpExchange exchange) throws IOException {
        String response = gson.toJson(service.getList());
        sendResponse(exchange, 200, response);
    }

    private void createArtist(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Artist artist = gson.fromJson(isr, Artist.class);
        Artist created = service.create(artist);
        sendResponse(exchange, 201, gson.toJson(created));
    }

    private void searchArtist(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        Optional<Artist> artist = service.get(id);
        if (artist.isPresent()) {
            sendResponse(exchange, 200, gson.toJson(artist.get()));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Artist not found\"}");
        }
    }

    private void deleteArtist(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        boolean deleted = service.delete(id);
        if (deleted) {
            sendResponse(exchange, 204, "{\"message\": \"Artist successfully deleted\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Artist not found\"}");
        }
    }

    private void updateArtist(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Artist artist = gson.fromJson(isr, Artist.class);
        artist.setId(id);

        boolean updated = service.update(artist);
        if (updated) {
            sendResponse(exchange, 200, gson.toJson(artist));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"Artist not found\"}");
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
