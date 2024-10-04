package me.sunera.jrest;

import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

public class JRest {

    private static HttpServer server;


    public JRest() {
        System.out.println("JRest is a simple REST API framework for Java.");
    }

    public boolean listen(int port) {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
            this.server.start();
            return true;
        } catch (Exception e) {
            System.out.println("Failed to start server: " + e.getMessage());
            return false;
        }
    }

    public static HttpServer getServer() {
        return server;
    }

    public void use(String path, Controller handler) {
        //
    }

    public static Router Router() {
        return new Router();
    }
}
