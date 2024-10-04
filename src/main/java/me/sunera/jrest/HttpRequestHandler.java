package me.sunera.jrest;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

class HttpRequestHandler implements HttpHandler {

    static Map<String, Map<String, Controller>> routes = new HashMap<>();

    public HttpRequestHandler(String method, String path, Controller handler) {
        System.out.println("Created HTTP Request Handler for " + method + " " + path);
        if (routes.containsKey(method)) {
            routes.get(method).put(path, handler);
        } else {
            Map<String, Controller> route = new HashMap<>();
            route.put(path, handler);
            routes.put(method, route);
        }

        System.out.println(routes);
    }

    public HttpRequestHandler(String method, String path, Controller[] handlers) {
        System.out.println("Created HTTP Request Handler for " + method + " " + path + " with middleware");

        if (routes.containsKey(method)) {
            routes.get(method).put(path, handlers[0]);
        } else {
            Map<String, Controller> route = new HashMap<>();
            route.put(path, handlers[0]);
            routes.put(method, route);
        }

        System.out.println(routes);
        
        for(int i = 0; i < handlers.length - 1; i++) {
            if (handlers[i] instanceof Middleware) {
                System.out.println("Middleware found");
                Middleware middleware = (Middleware) handlers[i];

                if(!(handlers[i + 1] instanceof Middleware)) {
                    System.out.println("Controller found");
                    Controller controller = (Controller) handlers[i + 1];
                    middleware.setNext(controller);
                    return;
                }

                Middleware nextMiddleware = (Middleware) handlers[i + 1];
                middleware.setNext(nextMiddleware);
            }
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        Map<String, Controller> methodRoutes = routes.get(requestMethod);

        if (methodRoutes == null) {
            System.out.println("Method not found");
            return;
        }
        
        String path = null;
        Map<String, String> pathParams = null;

        if (methodRoutes.containsKey(requestPath)) {
            path = requestPath;
        } else {
            for (String routePath : methodRoutes.keySet()) {
                if (routePath.contains(":")) {
                    String[] routeParts = routePath.split("/");
                    String[] requestParts = requestPath.split("/");

                    if (routeParts.length == requestParts.length) {
                        boolean isMatch = true;
                        Map<String, String> tempParams = new HashMap<>();

                        for (int i = 0; i < routeParts.length; i++) {
                            if (routeParts[i].startsWith(":")) {
                                String paramName = routeParts[i].substring(1);
                                tempParams.put(paramName, requestParts[i]);
                            } else if (!routeParts[i].equals(requestParts[i])) {
                                isMatch = false;
                                break;
                            }
                        }
                        if (isMatch) {
                            path = routePath;
                            pathParams = tempParams;
                            break;
                        }
                    }
                }
            }
        }

        if (path == null) {
            sendErrorResponse(exchange, 404, "Not Found");
            return;
        }

        Request request = new Request(exchange, pathParams);
        Response response = new Response(exchange);

        // System.out.println("Reached HTTP Handler -> Request Method: " + requestMethod + " Request Path: " + requestPath + " Request Path Params: " + pathParams);


        Controller handler = methodRoutes.get(path);

        if (handler instanceof Middleware) {
            Middleware middleware = (Middleware) handler;

            middleware.setRequest(request);
            middleware.setResponse(response);

            middleware.run(request, response, middleware);           
        } else {
            switch (request.getMethod()) {
                case "GET":
                    handler.get(request, response);
                    break;
                case "POST":
                    handler.post(request, response);
                    break;
                case "PUT":
                    handler.put(request, response);
                    break;
                case "DELETE":
                    handler.delete(request, response);
                    break;
                case "PATCH":
                    handler.patch(request, response);
                    break;
            }
        }
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }

    private Map<String, String> extractPathParams(String path, String URIPath) {
        Map<String, String> pathParams = new HashMap<>();

        if (path.contains(":")) {
            System.out.println("Contains :");

            String paramName = path.substring(path.indexOf(":") + 1);
            String userPath = path.substring(0, path.indexOf(":"));

            String[] pathParts = URIPath.split(userPath, 2);
            String lastPart = pathParts[pathParts.length - 1];

            System.out.println(paramName);
            System.out.println(lastPart);

            pathParams.put(paramName, lastPart);
        }

        return pathParams;
    }
}