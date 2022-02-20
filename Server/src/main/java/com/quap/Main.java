package com.quap;

import com.quap.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * Die Klasse stellt die Mainklasse des Servers dar und enthält die main-Methode
 */
public class Main {
    private static ServerSocket socket;
    private static int port;

    /**
     * Die Methode verarbeitet die Argumente, mit denen die Klasse aufgerufen wurde.
     * Wenn die Socket sich zu der ihr zugewiesenen Adresse binden kann, wird die Serverklasse aufgerufen
     * @param args entwerder ohne Argument, oder ein gültiger TCP Port (Standart ist 8192)
     */
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
            socket.bind(new InetSocketAddress("192.168.178.43", Main.port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Server(socket);
    }
}