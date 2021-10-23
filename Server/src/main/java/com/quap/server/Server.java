package com.quap.server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private int port;
    private boolean running = false;
    private Thread run, manage, send, receive;
    private final int MAX_ATTEMPTS = 5;

    private final List<ServerClient> clients = new ArrayList<ServerClient>();
    private final List<Integer> clientResponse = new ArrayList<Integer>();


    public Server(ServerSocket socket) { //TODO: for each Client create new Handler
        Socket clientSocket = null;
        try {
            clientSocket = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ClientHandler(clientSocket);
    }

    private void manageClients() {
        manage = new Thread("Manage") {
            public void run() {
                while (running) {
                    sendToAll("/i/server");
                    sendStatus();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < clients.size(); i++) {
                        ServerClient c = clients.get(i);
                        if (!clientResponse.contains(c.getID())) {
                            if (c.attempt >= MAX_ATTEMPTS) {
                                disconnect(c.getID(), false);
                            } else {
                                c.attempt++;
                            }
                        } else {
                            clientResponse.remove(c.getID());
                            c.attempt = 0;
                        }
                    }
                }
            }
        };
        manage.start();
    }

    private void sendStatus() {
        if (clients.size() <= 0) return;
        String users = "/u/";
        for (int i = 0; i < clients.size() - 1; i++) {
            users += clients.get(i).name + "/n/";
        }
        users += clients.get(clients.size() - 1).name + "/e/";
        sendToAll(users);
    }

    private void receive() {
        receive = new Thread("Receive") {
            public void run() {
                while (running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (SocketException e) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
                }
            }
        };
        receive.start();
    }

    private void sendToAll(String message) {
        if (message.startsWith("/m/")) {
            String text = message.substring(3);
            text = text.split("/e/")[0];
            System.out.println(text);
        }
        for (int i = 0; i < clients.size(); i++) {
            ServerClient client = clients.get(i);
            send(message.getBytes(), client.address, client.port);
        }
    }

    private void send(final byte[] data, final InetAddress address, final int port) {
        send = new Thread("Send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    private void send(String message, InetAddress address, int port) {
        message += "/e/";
        send(message.getBytes(), address, port);
    }

    private void process(DatagramPacket packet) {
        String string = new String(packet.getData());
        if (raw) System.out.println(string);
        if (string.startsWith("/c/")) {
            // UUID id = UUID.randomUUID();
            int id = UniqueIdentifier.getIdentifier();
            String name = string.split("/c/|/e/")[1];
            System.out.println(name + "(" + id + ") connected!");
            clients.add(new ServerClient(name, packet.getAddress(), packet.getPort(), id));
            String ID = "/c/" + id;
            send(ID, packet.getAddress(), packet.getPort());
        } else if (string.startsWith("/m/")) {
            sendToAll(string);
        } else if (string.startsWith("/d/")) {
            String id = string.split("/d/|/e/")[1];
            disconnect(Integer.parseInt(id), true);
        } else if (string.startsWith("/i/")) {
            clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
        } else {
            System.out.println(string);
        }
    }

    private void quit() {
        for (int i = 0; i < clients.size(); i++) {
            disconnect(clients.get(i).getID(), true);
        }
        running = false;
        socket.close();
    }

    private void disconnect(int id, boolean status) {
        ServerClient c = null;
        boolean existed = false;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getID() == id) {
                c = clients.get(i);
                clients.remove(i);
                existed = true;
                break;
            }
        }
        if (!existed) return;
        String message = "";
        if (status) {
            message = "Client " + c.name + " (" + c.getID() + ") @ " + c.address.toString() + ":" + c.port + " disconnected.";
        } else {
            message = "Client " + c.name + " (" + c.getID() + ") @ " + c.address.toString() + ":" + c.port + " timed out.";
        }
        System.out.println(message);
    }

    public void run() {
        running = true;
        System.out.println("Server started on port " + port);
        manageClients();
        receive();
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String text = scanner.nextLine();
            if (!text.startsWith("/")) {
                sendToAll("/m/Server: " + text + "/e/");
                continue;
            }
            text = text.substring(1);
            if (text.equals("raw")) {
                if (raw) System.out.println("Raw mode off.");
                else System.out.println("Raw mode on.");
                raw = !raw;
            } else if (text.equals("clients")) {
                System.out.println("Clients:");
                System.out.println("========");
                for (int i = 0; i < clients.size(); i++) {
                    ServerClient c = clients.get(i);
                    System.out.println(c.name + "(" + c.getID() + "): " + c.address.toString() + ":" + c.port);
                }
                System.out.println("========");
            } else if (text.startsWith("kick")) {
                String name = text.split(" ")[1];
                int id = -1;
                boolean number = true;
                try {
                    id = Integer.parseInt(name);
                } catch (NumberFormatException e) {
                    number = false;
                }
                if (number) {
                    boolean exists = false;
                    for (int i = 0; i < clients.size(); i++) {
                        if (clients.get(i).getID() == id) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) disconnect(id, true);
                    else System.out.println("Client " + id + " doesn't exist! Check ID number.");
                } else {
                    for (int i = 0; i < clients.size(); i++) {
                        ServerClient c = clients.get(i);
                        if (name.equals(c.name)) {
                            disconnect(c.getID(), true);
                            break;
                        }
                    }
                }
            } else if (text.equals("help")) {
                printHelp();
            } else if (text.equals("quit")) {
                quit();
            } else {
                System.out.println("Unknown command.");
                printHelp();
            }
        }
        scanner.close();
    }
}
