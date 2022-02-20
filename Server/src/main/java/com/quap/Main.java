package com.quap;

import com.quap.server.Server;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

/**
 * TODO
 * Die Klasse stellt die Mainklasse des Servers da und enthält die main-Methode
 */
public class Main {
    private static ServerSocket socket;
    private static int port;

    /**
     * TODO
     * Die Methode verarbeitet die Argumente, mit denen die Klasse aufgerufen wurde.
     * Wenn die Socket sich zu der ihr zugewiesenen Adresse binden kann, wird die Serverklasse aufgerufen
     * @param args
     */
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");

        System.out.println("Printing information about the available interfaces...\n");
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface iface : Collections.list(nets)) {
                if (iface.isUp()) {
                    System.out.println("Interface name: " + iface.getDisplayName());
                    System.out.println("\tInterface addresses: ");
                    for (InterfaceAddress addr : iface.getInterfaceAddresses()) {
                        System.out.println("\t\t" + addr.getAddress().toString());
                    }
                    System.out.println("\tMTU: " + iface.getMTU());
                    System.out.println("\tSubinterfaces: " + Collections.list(iface.getSubInterfaces()));
                    System.out.println("\tis loopback: " + iface.isLoopback());
                    System.out.println("\tis virtual: " + iface.isVirtual());
                    System.out.println("\tis point to point: " + iface.isPointToPoint());
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
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