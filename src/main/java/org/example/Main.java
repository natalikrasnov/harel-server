package org.example;

import org.example.http.HttpServerManager;

public class Main {

    public static void main(String[] args) {
        HttpServerManager serverManager = new HttpServerManager();
        System.out.println("--start server--");
        serverManager.start();
    }
}