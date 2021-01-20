package com.example.grpc.server.Impl;

import com.example.grpc.interfaces.GreetingReply;
import com.example.grpc.interfaces.GreetingRequest;
import com.example.grpc.interfaces.GreetingServiceGrpc;

import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase  {

    @Override 
    public void sayHi(GreetingRequest req, StreamObserver<GreetingReply> responseObserver){ 
        GreetingReply reply = GreetingReply.newBuilder().setMessage(("Hi " + req.getName())).build(); 
        responseObserver.onNext(reply); 
        responseObserver.onCompleted(); 
    } 
}
