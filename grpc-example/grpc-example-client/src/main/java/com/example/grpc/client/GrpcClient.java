package com.example.grpc.client;

import com.example.grpc.interfaces.GreetingReply;
import com.example.grpc.interfaces.GreetingRequest;
import com.example.grpc.interfaces.GreetingServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GrpcClient {

    private final ManagedChannel channel; 
    private final GreetingServiceGrpc.GreetingServiceBlockingStub blockingStub; 
    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());

    public GrpcClient(String host, int port){ 
        channel = ManagedChannelBuilder.forAddress(host, port) 
                                       .usePlaintext() 
                                       .build();

        blockingStub = GreetingServiceGrpc.newBlockingStub(channel); 
    }

    public void shutdown() throws InterruptedException { 
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS); 
    }

    public void greet(String name){ 
        GreetingRequest request = GreetingRequest.newBuilder().setName(name).build(); 
        GreetingReply response;
        
        try{ 
            response = blockingStub.sayHi(request); 
        } catch (StatusRuntimeException e) { 
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus()); 
            return;
        }
        
        logger.info("Greeting: " + response.getMessage()); 
    }

    public static void main(String[] args) throws InterruptedException { 
        GrpcClient client = new GrpcClient("localhost", 50051); 
        
        try{ 
            String user = "GRPC"; 
            if (args.length > 0){
                user = args[0];
            }
            client.greet(user); 
        }finally { 
            client.shutdown(); 
        } 
    } 
}
