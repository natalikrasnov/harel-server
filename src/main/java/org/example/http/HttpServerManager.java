package org.example.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager extends Thread {

    private final int port = 8080;
    private HttpServer server = null;

    @Override
    public void run() {
        System.out.println("connecting...");
        startServerListener();
    }

    public void startServerListener(){
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("server start on port "+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addRouters();
        server.setExecutor(null);


        server.start();
    }

    private void addRouters(){
        server.createContext(HttpRouters.login.PATH, HttpRouters.login.HANDLER);
        server.createContext(HttpRouters.getCustomers.PATH, HttpRouters.getCustomers.HANDLER);
        server.createContext(HttpRouters.getCustomer.PATH, HttpRouters.getCustomer.HANDLER);
        server.createContext(HttpRouters.editCustomer.PATH, HttpRouters.editCustomer.HANDLER);
    }

}
