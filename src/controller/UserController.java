package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.User;
import service.UserService;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class UserController implements HttpHandler {
    private final UserService service = new UserService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path =  exchange.getRequestURI().getPath();

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        if (method.equalsIgnoreCase("GET")) {
            if (path.equals("/users")) {
                listUser(exchange);
            } else if (path.startsWith("/users/")) {
                searchUser(exchange, path);
            }
        } else if (method.equalsIgnoreCase("POST")) {
            createUser(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            updateUser(exchange, path);
        } else if (method.equalsIgnoreCase("DELETE")) {
            deleteUser(exchange, path);
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void listUser(HttpExchange exchange) throws IOException {
        String response = gson.toJson(service.getList());
        sendResponse(exchange, 200, response);
    }

    private void createUser(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        User user = gson.fromJson(isr, User.class);
        User created = service.create(user);
        sendResponse(exchange, 201, gson.toJson(created));
    }

    private void searchUser(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        Optional<User> user = service.get(id);
        if (user.isPresent()) {
            sendResponse(exchange, 200, gson.toJson(user.get()));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"User not found\"}");
        }
    }

    private void deleteUser(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        boolean deleted = service.delete(id);
        if (deleted) {
            sendResponse(exchange, 204, "{\"message\": \"User successfully deleted\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\": \"User not found\"}");
        }
    }

    private void updateUser(HttpExchange exchange, String path) throws IOException {
        String id = extractIdFromUrl(path);
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        User user = gson.fromJson(isr, User.class);
        user.setId(id);

        boolean updated = service.update(user);
        if (updated) {
            sendResponse(exchange, 200, gson.toJson(user));
        } else {
            sendResponse(exchange, 404, "{\"error\": \"User not found\"}");
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
