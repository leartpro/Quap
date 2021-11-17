package com.quap.desktopapp;


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
    @Override
    public void init() {
        final ExecutorService executor = Executors.newCachedThreadPool();
        final CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);
        List<Map<String, Future<Boolean>>> resultList = new ArrayList<>();

        ConnectionWindowController init = new ConnectionWindowController();

        for (Future<Boolean> booleanFuture : Arrays.asList(new HashMap<String, Future<Boolean>>().put(
                "connect",
                completionService.submit(
                        init.connect()
                )
        ), new HashMap<String, Future<Boolean>>().put(
                "openConnection",
                completionService.submit(
                        init.openConnection()
                )
        ), new HashMap<String, Future<Boolean>>().put(
                "confirmConnection",
                completionService.submit(
                        init.confirmConnection()
                )
        ), new HashMap<String, Future<Boolean>>().put(
                "launchWindow",
                completionService.submit(
                        init.launchWindow()
                )
        ))) {
            resultList.add((Map<String, Future<Boolean>>) booleanFuture
            );
        }

        for (Map<String, Future<Boolean>> pair : resultList) {
            Optional<String> optional = pair.keySet().stream().findFirst();
            if (!optional.isPresent()) {
                return;
            }
            String key = optional.get();

            System.out.printf("Value is: %d%n", key);

            Future<Boolean> future = pair.get(key);
            Boolean result = null;
            try {
                result = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            Boolean isDone = future.isDone();

            System.out.printf("Result is %d%n", result);
            System.out.printf("Task done: %b%n", isDone);
            System.out.println("--------------------");
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
