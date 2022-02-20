package com.quap.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Wartet auf eingehende Verbindungen an der ServerSocket
 * und verwaltet die ClientHandler in einem ExecutorService
 */
public class Server{
    private final ExecutorService service;
    private final ServerSocket socket;
    private Thread receive;

    private final List<ClientHandler> handler = new ArrayList<>();
    private boolean status;


    /**
     * Im Konstruktor wird die Socket und der Status gesetzt,
     * als auch die receiveConnection() Methode gestartet
     * @param socket die ServerSocket
     */
    public Server(ServerSocket socket) {
        service = Executors.newCachedThreadPool();
        this.socket = socket;
        status = true;
        receiveConnection();
    }

    /**
     * Die Methode wirft einen Thread aus, indem auf einegende Verbindungsanfragen gewartet wird
     */
    public void receiveConnection() {
        Server server = this;
        receive = new Thread("Receive") {
            public void run() {
                while (status) {
                    Socket client = null;
                    try {
                        System.out.println("Listening on: " + socket.getLocalSocketAddress());
                        client = socket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                        terminate(false);
                    }
                    assert client != null;
                    System.out.print("\r\nNew connection from " + client.getInetAddress() + ":" + client.getPort());
                    System.out.println(" to " + socket.getInetAddress() + ":" + socket.getLocalPort());
                    ClientHandler clientHandler = new ClientHandler(client, UniqueIdentifier.getIdentifier(), server);
                    service.submit(clientHandler);
                    handler.add(clientHandler);
                    if(socket.isClosed()) {
                        status = false;
                        terminate(true);
                    }
                }
            }
        };
        receive.start();
    }

    /**
     * Bringt den ClientHandler mit der entsprechenden Id, dazu die Verbindung zum Client zu trennen
     * @param id Die UUID des ClientHandlers
     */
    private void disconnect(int id) {
        ClientHandler c;
        for (int i = 0; i < handler.size(); i++) {
            if (handler.get(i).getID() == id) {
                c = handler.get(i);
                c.disconnect(true);
                handler.remove(i);
                break;
            }
        }
    }

    /**
     * Bricht alle Verbindungen ab und schließt den Server
     * @param status Der status, mit dem der Server sich schließen soll
     */
    public void terminate(boolean status) {
        receive.interrupt();
        for (ClientHandler clientHandler : handler) {
            disconnect(clientHandler.getID());
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (status) {
            System.out.println(LocalTime.now().toString().substring(0, LocalTime.now().toString().indexOf("."))
                    + " :Attempting to shutdown");
            service.shutdown();
            try {
                if (service.awaitTermination(4, TimeUnit.SECONDS)) {
                    System.out.println(LocalTime.now().toString().substring(0, LocalTime.now().toString().indexOf("."))
                            + " :Pool terminate");
                    System.out.println(Thread.activeCount() + ", " + Thread.getAllStackTraces() + ", "
                            + Thread.getAllStackTraces().hashCode() + ", " + Thread.currentThread().getState());
                    service.shutdownNow();
                } else if (!service.awaitTermination(4, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                    System.out.println(Thread.activeCount() + ", " + Thread.getAllStackTraces() + ", "
                            + Thread.getAllStackTraces().hashCode() + ", " + Thread.currentThread().getState());
                    service.shutdownNow();
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        } else {
            Runtime.getRuntime().addShutdownHook(new Thread("shutdown") {
                public void run() {
                    if (!service.isShutdown() || !service.isTerminated()) {
                        service.shutdown();
                        try {
                            if (service.awaitTermination(4, TimeUnit.SECONDS)) {
                                System.out.println("Main: Pool terminate");
                                service.shutdownNow();
                            } else if (!service.awaitTermination(4, TimeUnit.SECONDS)) {
                                System.err.println("Pool did not terminate");
                                service.shutdownNow();
                                System.out.println(Thread.activeCount() + ", " + Thread.getAllStackTraces() + ", "
                                        + Thread.getAllStackTraces().hashCode() + ", " + Thread.currentThread().getState());
                            }
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            );
        }
    }

    /**
     * Sendet über den ClientHandler, welcher den Nutzer
     * mit der entsprechenden userID verwaltet die Nachricht
     * @param userID die Id des Nutzers
     * @param message Die zu sendene Nachricht
     */
    public void forwardMessage(int userID, String message) {
        for (ClientHandler clientHandler : handler) {
            if (clientHandler.getUserID() == userID) {
                clientHandler.send(message);
            }
        }
    }
}
