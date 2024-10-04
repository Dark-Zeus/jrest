package me.sunera.jrest;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Request {
    private final HttpExchange exchange;
    private final Map<String, String> pathParams;

    public Request(HttpExchange exchange, Map<String, String> pathParams) {
        this.exchange = exchange;
        this.pathParams = pathParams;
    }

    public String getMethod() {
        return exchange.getRequestMethod();
    }

    public URI getUri() {
        return exchange.getRequestURI();
    }

    public Map<String, List<String>> getHeaders() {
        return exchange.getRequestHeaders();
    }

    public String getBody() throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        return new Scanner(requestBody, StandardCharsets.UTF_8.name()).useDelimiter("\\A").next();
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

    public String getQueryParam(String key) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1 && entry[0].equals(key)) {
                    return entry[1];
                }
            }
        }
        return null;
    }

    public void setHeader(String key, String value) {
        exchange.getResponseHeaders().set(key, value);
    }

    public void setBody(String body) throws IOException {
        exchange.sendResponseHeaders(200, body.getBytes().length);
        exchange.getResponseBody().write(body.getBytes());
        exchange.getResponseBody().close();
    }

    public void setParams(Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            exchange.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public String getParam(String key) {
        return (String) exchange.getAttribute(key);
    }
}