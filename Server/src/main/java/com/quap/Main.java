package com.quap;

import com.quap.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Main {
    private static ServerSocket socket;
    private static int port;

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        if (args.length==0) {
            Main.port = 8192;
        } else if(args.length==1) {
            Main.port = Integer.parseInt(args[0]);
        } else {
            System.err.println("Usage1: java -jar QuapServer.jar");
            System.err.println("Usage2: java -jar QuapServer.jar <PORT[int]>");
        }
        try {
            socket = new ServerSocket();
            socket.bind(new InetSocketAddress("192.168.178.69", Main.port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Server(socket);
    }
}