package com.quap;

import java.io.IOException;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final int[] defaultPorts = new int[]{8, 10, 14, 16, 26, 40, 80, 9050, 9051, 9091};
    private static final String regexPort = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
    private static final String regexAddress = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b";
    private static ServerSocket socket;
    private static int port = defaultPorts[0];
    private static  final InetAddress address;
    private static int backlog = 65536;

    static {
        assert socket != null;
        address = socket.getInetAddress();

        try {
            backlog = socket.getReceiveBufferSize();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws PortUnreachableException, UnknownHostException {

        socket = switch (args.length) {
            case 0 -> defaultConfiguration();
            case 1 -> portConfiguration(args[0]);
            case 2 -> customConfiguration(args[0], args[1]);
            default -> throw new IllegalStateException("Unexpected value: " + args.length);
        };
        //TODO: open connection

    }

    private static ServerSocket customConfiguration(String arg1, String arg2) throws PortUnreachableException, UnknownHostException {
        int port = defaultPorts[0];
        InetAddress address = socket.getInetAddress();
        Pattern pattern = Pattern.compile(regexPort);
        Matcher matcher = pattern.matcher(arg1);
        if(!matcher.find()) {
            throw new PortUnreachableException();
        } else {
            Integer.parseInt(arg1);
        }
        pattern = Pattern.compile(regexAddress);
        matcher = pattern.matcher(arg2);
        if(!matcher.find()) {
            throw new UnknownHostException();
        } else {
            address = InetAddress.getByName(arg2);
        }
        try {
            socket = new ServerSocket(port, backlog, address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    private static ServerSocket portConfiguration(String arg) throws PortUnreachableException {
        int port;
        Pattern pattern = Pattern.compile(regexPort);
        Matcher matcher = pattern.matcher(arg);
        if(!matcher.find()) {
                throw new PortUnreachableException();
        } else {
            port = Integer.parseInt(arg);
        }
        try {
            socket = new ServerSocket(port, backlog, address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    private static ServerSocket defaultConfiguration() {

        for (int p : defaultPorts) {
            try {
                socket = new ServerSocket(p);
                port = p;
            } catch (IOException e) {
                continue;
            }

            try {
                socket = new ServerSocket(port, backlog, address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }
}
