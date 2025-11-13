package http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import controller.*;

public class Server {
    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/users", new UserController());
        server.createContext("/follows", new FollowController());
        server.createContext("/genres", new GenreController());
        server.createContext("/artists", new ArtistController());
        server.createContext("/albums", new AlbumController());

        server.setExecutor(null);
        System.out.println("Server started on http://localhost:8080");
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
