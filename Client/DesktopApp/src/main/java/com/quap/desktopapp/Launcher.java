package com.quap.desktopapp;


import com.quap.client.data.ConfigReader;
import com.quap.controller.scene.ConnectionWindowController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.*;


/*
 * Launcher application class.
 */
public class Launcher extends Application {

    public static Stage primaryStage = null;

    //TODO: work with future results

    /* //usage of runtime package
       ExecutorService service = Executors.newFixedThreadPool(5);
       FutureExecutor executor = new FutureExecutor(service);
       ListenableFuture<Integer> future = executor.submit(new DelayedRandomNumber(1000));
       future.addCallback(new ResultPrinter());

       service.shutdown();
        */
    @Override //TODO: check system vars, folder systems, hardware usw...
    public void init() {
        final ExecutorService executor = Executors.newCachedThreadPool();
        final CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);
        List<HashMap<Integer, Future<Boolean>>> taskList = new ArrayList<>();
        HashMap<Integer, Future<Boolean>> tasks = new HashMap<>();

        ConnectionWindowController init = new ConnectionWindowController();
        executor.execute(() -> {
            ConfigReader configReader = new ConfigReader("anonym");
            configReader.createUser();
            //UserdataReader dataReader = new UserdataReader("anonym", "toor");
        });

        tasks.put(0, completionService.submit(init.connect()));
        taskList.add(tasks);
        for(int i = 0; i < 4; i++) {
            for (Map<Integer, Future<Boolean>> pair : taskList) {
                System.out.println(pair);
                taskList.remove(pair);
                Optional<Integer> optional = pair.keySet().stream().findFirst();
                if (!optional.isPresent()) {
                    return;
                }
                Integer key = optional.get();
                Future<Boolean> future = pair.get(key);
                Boolean result = false;
                try {
                    result = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (result) {
                    init.addProgress();
                    try { //TODO: does slow the init process for testing and debugging
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switch (optional.get()) {
                        case 0 -> {
                            System.out.println(0);
                            tasks = new HashMap<>();
                            tasks.put(1, completionService.submit(init.openConnection()));
                            taskList.add(tasks);
                        }
                        case 1 -> {
                            System.out.println(1);
                            tasks = new HashMap<>();
                            tasks.put(2, completionService.submit(init.confirmConnection()));
                            taskList.add(tasks);
                        }
                        case 2 -> {
                            System.out.println(2);
                            tasks = new HashMap<>();
                            tasks.put(3, completionService.submit(init.launchWindow()));
                            taskList.add(tasks);
                        }
                        case 3 -> {
                            System.out.println("All Tasks completed successful");
                            return;
                        }
                    }
                }
            }
        }

        while (!executor.isTerminated()) {
            Future<Boolean> future = null;
            try {
                future = completionService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                assert future != null;
                if(!future.get()) {
                    executor.shutdownNow();
                    System.err.println("Client initialisation failed");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    @Override
    public void start(Stage primaryStage) {
        Launcher.primaryStage = primaryStage;
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", LauncherPreloader.class.getCanonicalName());
        launch(args);
    }
}
