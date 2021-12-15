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

public class Server{
    private final ExecutorService service;
    private final ServerSocket socket;
    private Thread manage;
    private Thread receive;

    private final List<ClientHandler> handler = new ArrayList<>();
    private final List<ClientHandler> onlineHandlers = new ArrayList<>();
    private final boolean status;


    public Server(ServerSocket socket) {
        service = Executors.newCachedThreadPool();
        this.socket = socket;
        status = true;
        //manageClients();
        receiveConnection();
        //TODO: db connection
        //TODO: config reader
        /*ConfigReader configReader = new ConfigReader();
        configReader.loadDefaultProperties();*/

        /*
        TODO: Server wirft Threads als Future raus und startet jedes mal neu, solange status==true!

         */
        /*
        service.execute(new CommandListener());
        Scanner scanner = new Scanner(System.in);
        while (status) {
            String text = scanner.nextLine();
            //TODO: manage commands
        }
        scanner.close();
         */
    }

    /*public void manageClients() {
        manage = new Thread("Manage") {
            public void run() {
                while (status) {
                    for (int i = 0; i < handler.size(); i++) {
                        System.out.println(handler.get(i).getClient().getName() + " is online.");
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        manage.start();
    }*/

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
                    }
                    assert client != null;
                    System.out.print("\r\nNew connection from " + client.getInetAddress() + ":" + client.getPort());
                    System.out.println(" to " + socket.getInetAddress() + ":" + socket.getLocalPort());
                    ClientHandler clientHandler = new ClientHandler(
                            client,
                            UniqueIdentifier.getIdentifier(), //TODO: each Client handler stores his userID
                            server);
                    service.submit(clientHandler);
                    handler.add(clientHandler);
                    //TODO: work with return
                    //when return and result is not null -> submit a new ClientHAndler, because the previous failed
                }
            }
        };
        receive.start();
    }

    private void disconnect(int id, boolean status) {
        ClientHandler c;
        for (int i = 0; i < handler.size(); i++) {
            if (handler.get(i).getID() == id) {
                c = handler.get(i);
                c.disconnect();
                handler.remove(i);
                break;
            }
        }
    }

    public void terminate(boolean status) {
        for (ClientHandler clientHandler : handler) {
            disconnect(clientHandler.getID(), true);
        }
        status = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (status) { //TODO: status is always false
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
                //TODO: handle exception
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

    public void forwardMessage(int userID, String message) {
        System.out.println("forward message from " + userID);
        //TODO: get all ClientHandler by userID
        // then sends to each Client content + senderID
        for (ClientHandler clientHandler : handler) {
            System.out.println("UserID: " + clientHandler.getUserID());
            if (clientHandler.getUserID() == userID) {
                System.out.println("Send Message from Client:" + userID + " to Client:" + clientHandler.getUserID());
                clientHandler.send(message);
            }
        }
    }
}
