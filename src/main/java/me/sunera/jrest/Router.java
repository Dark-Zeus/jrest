package me.sunera.jrest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Router {
    public void get(String path, Controller handler) {
        JRest.getServer().createContext(getAbsolutePath(path), new HttpRequestHandler("GET", path, handler));
        System.out.println("Created GET context for " + path);
    }

    public void get(String path, Controller ...handlers) {
        JRest.getServer().createContext(getAbsolutePath(path), new HttpRequestHandler("GET", path, handlers));
        System.out.println("Created GET context for " + path);
    }

    public void post(String path, Controller handler) {
        JRest.getServer().createContext(getAbsolutePath(path), new HttpRequestHandler("POST", path, handler));
        System.out.println("Created POST context for " + path);
    }

    public void put(String path, Controller handler) {
        JRest.getServer().createContext(getAbsolutePath(path), new HttpRequestHandler("PUT", path, handler));
    }

    public void delete(String path, Controller handler) {
        JRest.getServer().createContext(getAbsolutePath(path), new HttpRequestHandler("DELETE", path, handler));
    }

    public void patch(String path, Controller handler) {
        JRest.getServer().createContext(getAbsolutePath(path), new HttpRequestHandler("PATCH", path, handler));
    }

    public String getAbsolutePath(String path) {
        if (path.contains(":")) {
            return path.substring(0, path.indexOf(":"));
        }
        return path;
    }
}
