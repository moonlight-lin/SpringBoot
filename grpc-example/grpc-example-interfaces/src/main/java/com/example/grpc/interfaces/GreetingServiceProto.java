// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: GreetingService.proto

package com.example.grpc.interfaces;

public final class GreetingServiceProto {
  private GreetingServiceProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GreetingService_GreetingRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GreetingService_GreetingRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GreetingService_GreetingReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GreetingService_GreetingReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025GreetingService.proto\022\017GreetingService" +
      "\"\037\n\017GreetingRequest\022\014\n\004name\030\001 \001(\t\" \n\rGre" +
      "etingReply\022\017\n\007message\030\001 \001(\t2^\n\017GreetingS" +
      "ervice\022K\n\005SayHi\022 .GreetingService.Greeti" +
      "ngRequest\032\036.GreetingService.GreetingRepl" +
      "y\"\000B;\n\033com.example.grpc.interfacesB\024Gree" +
      "tingServiceProtoP\001\242\002\003HLWb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_GreetingService_GreetingRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_GreetingService_GreetingRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GreetingService_GreetingRequest_descriptor,
        new java.lang.String[] { "Name", });
    internal_static_GreetingService_GreetingReply_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_GreetingService_GreetingReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GreetingService_GreetingReply_descriptor,
        new java.lang.String[] { "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}