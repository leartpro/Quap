package com.quap.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable  {
    private final ExecutorService service;
    private final ServerSocket socket;
    private final Thread run;
    private Thread manage;
    private Thread receive;

    private final List<ClientHandler> handler = new ArrayList<>();
    private final List<ClientHandler> onlineHandlers = new ArrayList<>();
    private boolean status;


    public Server(ServerSocket socket) {
        service = Executors.newCachedThreadPool();
        this.socket = socket;
        //try {
            //socket.bind(new InetSocketAddress("192.168.178.1", 8192));
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        run = new Thread( this, "Server");
        run.start();
    }

    public void run() {
        status = true;
        manageClients();
        receiveConnection();
        //TODO: db connection
        /*
        TODO: Server wirft Threads als Future raus und startet jedes mal neu, solange status==true!

         */
        service.execute(new CommandListener());
        Scanner scanner = new Scanner(System.in);
        while (status) {
            String text = scanner.nextLine();
            //TODO: manage commands
        }
        scanner.close();
    }


    public void manageClients() {
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
    }

    public void receiveConnection() {
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
                    service.execute(new ClientHandler(client, UniqueIdentifier.getIdentifier()));
                }
            }
        };
        receive.start();
    }

    private void disconnect(int id, boolean status) {
        ClientHandler c = null;
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
        for (int i = 0; i < handler.size(); i++) {
            disconnect(handler.get(i).getID(), true);
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

    /*public static class ListenableFuture<V> {
        private FutureCallback<V> callback;
        private V result;
        private Throwable failure;
        private boolean isCompleted;

        public void addCallback(FutureCallback<V> callback) {
            this.callback = callback;
            resolve();
        }

        public void setResult(V result) {
            this.result = result;
            isCompleted = true;
            resolve();
        }

        public void setFailure(Throwable failure) {
            this.failure = failure;
            isCompleted = true;
            resolve();
        }

        private void resolve() {
            if (callback != null && isCompleted) {
                if (failure == null) {
                    callback.onSuccess(result);
                } else {
                    callback.onFailure(failure);
                }
            }
        }
    }

    public static interface FutureCallback<V> {
        void onSuccess(V result);

        void onFailure(Throwable failure);
    }

    public static class ResultPrinter implements FutureCallback<Integer> {
        @Override
        public void onSuccess(Integer result) {
            System.out.println("Result: " + result);
        }

        @Override
        public void onFailure(Throwable failure) {
            failure.printStackTrace();
        }
    }

    public static class FutureExecutor {
        private ExecutorService executor;

        public FutureExecutor(ExecutorService executor) {
            this.executor = executor;
        }

        public <V> ListenableFuture<V> submit(final Callable<V> callable) {
            final ListenableFuture<V> future = new ListenableFuture<>();
            executor.submit(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    try {
                        V result = callable.call();
                        future.setResult(result);
                        return result;
                    } catch (Exception e) {
                        future.setFailure(e);
                        throw e;
                    }
                }
            });

            return future;
        }
    }*/



}
