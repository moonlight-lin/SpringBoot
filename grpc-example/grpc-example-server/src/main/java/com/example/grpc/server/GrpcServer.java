package com.example.grpc.server;

import java.io.IOException;
import java.util.logging.Logger;

import com.example.grpc.server.Impl.GreetingServiceImpl;

import io.grpc.Server;
import io.grpc.ServerBuilder;


public class GrpcServer {

    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    private int port = 50051; 
    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(port) 
                              .addService(new GreetingServiceImpl()) 
                              .build() 
                              .start();

        logger.info("Server started, listening on "+ port);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override 
            public void run(){
                System.err.println("*** shutting down gRPC server since JVM is shutting down"); 
                GrpcServer.this.stop(); 
                System.err.println("*** server shut down"); 
            } 
        }); 
    }

    private void stop() { 
        if (server != null){ 
            server.shutdown(); 
        } 
    }

    private void blockUntilShutdown() throws InterruptedException { 
        if (server != null){ 
            server.awaitTermination(); 
        } 
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final GrpcServer server = new GrpcServer(); 
        server.start(); 
        server.blockUntilShutdown(); 
    }
} 
