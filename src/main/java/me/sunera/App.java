package me.sunera;

// Make a rest controller without any 3rd party library

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.naming.ldap.Control;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import me.sunera.jrest.JRest;
import me.sunera.jrest.Middleware;
import me.sunera.jrest.Router;
import me.sunera.jrest.Controller;
import me.sunera.jrest.Request;
import me.sunera.jrest.Response;

public class App {
    public static void main(String[] args) throws Exception {
        JRest app = new JRest();

        app.listen(8000);
        
        Router router = JRest.Router();
        router.get("/api/user", new AuthenticateUser(),new AuthenticateUser(),new AuthenticateUser(), new APIHandler());
        router.get("/api/user/hs", new APIHandler());
    }

    static class AuthenticateUser extends Middleware {
        public void run(Request request, Response response, Controller next) {
            System.out.println("Authenticating user");

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("id", "1");
            request.setParams(params);
            next.run();
        }
    }

    static class APIHandler implements Controller {

        public void get(Request request, Response response) {

            try {
                System.out.println(request.getParam("id"));
                response.setHeader("Content-Type", "application/json");
                response.send(200, "{\"message\": \"Get2\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void post(Request request, Response response) {
            try {
                System.out.println(request.getQueryParam("id"));
                response.setHeader("Content-Type", "application/json");
                response.send(200, "{\"message\": \"Post\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void put(Request request, Response response) {
        }

        public void delete(Request request, Response response) {
        }

        public void patch(Request request, Response response) {
        }
    }
}