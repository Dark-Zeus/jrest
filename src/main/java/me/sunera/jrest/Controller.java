package me.sunera.jrest;

import java.io.IOException;

import me.sunera.*;

public interface Controller {

    default public void get(Request request, Response response) throws IOException {
        response.send(405, "{\"message\": \"GET Method is Not Allowed for the path:" + 
            request.getUri() + "\"}");
    }

    default public void post(Request request, Response response) throws IOException{
        response.send(405, "{\"message\": \"POST Method is Not Allowed for the path:" + 
            request.getUri() + "\"}");
    }

    default public void put(Request request, Response response) throws IOException{
        response.send(405, "{\"message\": \"PUT Method is Not Allowed for the path:" + 
        request.getUri() + "\"}");
    }

    default public void delete(Request request, Response response) throws IOException{
        response.send(405, "{\"message\": \"DELETE Method is Not Allowed for the path:" + 
            request.getUri() + "\"}");
    }

    default public void patch(Request request, Response response) throws IOException{
        response.send(405, "{\"message\": \"PATCH Method is Not Allowed for the path:" + 
            request.getUri() + "\"}");
    }

    default void setRequest(Request request){

    }

    default void setResponse(Response response){

    }

    default void run(){
        
    }

    default void run(Request request, Response response){

    }

}
