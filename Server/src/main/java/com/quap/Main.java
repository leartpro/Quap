package com.quap;

import com.quap.server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Main {
    private static ServerSocket socket;

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        int port=8192;
        if (args.length==0) {
            new Main(port);
        } else if(args.length==1) {
            new Main(Integer.parseInt(args[0]));
        } else if(args.length==2) {
            new Main(Integer.parseInt(args[0]), 0, args[1]);
        } else if(args.length==3) {
            new Main(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
        } else {
            System.err.println("Usage1: java -jar QuapServer.jar");
            System.err.println("Usage2: java -jar QuapServer.jar <PORT[int]>");
            System.err.println("Usage3: java -jar QuapServer.jar <PORT[int]> <ADDRESS[String]>");
            System.err.println("Usage4: java -jar QuapServer.jar <PORT[int]> <BACKLOG[int]> <ADDRESS[String]>");
        }
    }

    public Main(int port) {
        try {
            socket = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.bind(new InetSocketAddress("192.168.178.69", port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Server(socket);
    }

    public Main(int port, int backlog, String address) {
        try {
            socket = new ServerSocket(port, backlog, InetAddress.getByName(address));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("No IP address for the host could be found, or a scope_id was specified for a global IPv6 address.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("A security manager exists and its checkListen method doesn't allow the operation.");
        }
        new Server(socket);
    }
}