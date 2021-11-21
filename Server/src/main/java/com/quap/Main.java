package com.quap;

import com.quap.server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Main { //TODO: mdns
    private static ServerSocket socket;

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        //TODO: read from properties file
        /*String adapter_name="Ethernet";
        String ip_address="192.168.56.1";
        String subnet_mask="255.255.255.0";
        String default_gateway="192.168.178.1";
        String[] command =
                {
                        "cmd",
                };
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println("netsh int ip set address "+adapter_name+" static "+ip_address+" "+subnet_mask+" "+default_gateway);
            stdin.close();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
    /*
    private static class SyncPipe implements Runnable {
        public SyncPipe(InputStream istrm, OutputStream ostrm) {
            istrm_ = istrm;
            ostrm_ = ostrm;
        }
        public void run() {
            try
            {
                final byte[] buffer = new byte[1024];
                for (int length = 0; (length = istrm_.read(buffer)) != -1; )
                {
                    ostrm_.write(buffer, 0, length);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        private final OutputStream ostrm_;
        private final InputStream istrm_;
    }
    */
}