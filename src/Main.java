import database.ConnectionFactory;
import http.Server;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Closing server...");
                server.stop();
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}