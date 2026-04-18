package com.placement.main;

import com.placement.servlet.AdminServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class MainApp {

    public static void main(String[] args) throws Exception {

        // Create server on port 8080
        Server server = new Server(8080);

        // Setup servlet context
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        // Map AdminServlet to /api/admins/*
        context.addServlet(new ServletHolder(new AdminServlet()), "/api/admins/*");

        server.setHandler(context);
        server.start();
        System.out.println("Server started successfully!");
        System.out.println("URL: http://localhost:8080/api/admins");

        server.join(); // Keep server running
    }
}