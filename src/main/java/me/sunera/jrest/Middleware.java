package me.sunera.jrest;

import java.io.IOException;

public abstract class Middleware implements Controller {

    private Controller next;
    private Request request;
    private Response response;

    public void run() {
        if(this.next instanceof Controller && !(this.next instanceof Middleware)){
            System.out.println("Middleware next is a controller");
            Controller nextController = (Controller) this.next;
            System.out.println(nextController);
            runController(request, response, nextController);
        }else{
            System.out.println("Middleware next is not a controller");
            System.out.println(this.next);
            Middleware nextMiddleware = (Middleware) this.next;
            nextMiddleware.setRequest(this.request);
            nextMiddleware.setResponse(this.response);
            nextMiddleware.run(this.request, this.response, this.next);
        }
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setNext(Controller next) {
        this.next = next;
    }

    public Controller getNext() {
        return this.next;
    }

    private static void runController(Request request, Response response, Controller nextController) {
        System.out.println("Running controller");
        System.out.println(request.getMethod());
        switch (request.getMethod()) {
            case "GET":
                try {
                    nextController.get(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "POST":
                try {
                    nextController.post(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "PUT":
                try {
                    nextController.put(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "DELETE":
                try {
                    nextController.delete(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "PATCH":
                try {
                    nextController.patch(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    
    public abstract void run(Request request, Response response, Controller next);
}
