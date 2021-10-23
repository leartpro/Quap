package com.quap.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server(ServerSocket socket) { //TODO: for each Client create new Handler
        Socket clientSocket = null;
        try {
            clientSocket = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new ClientHandler(clientSocket);
    }
}
