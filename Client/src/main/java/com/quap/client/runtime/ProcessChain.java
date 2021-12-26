package com.quap.client.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public abstract class ProcessChain <R, T>{
    private final List<HashMap<Integer, Future<T>>> taskList = new ArrayList<>();
    private final CompletionService<T> completionService;
    public abstract R process(HashMap<Integer, Future<T>>... tasks);

    public ProcessChain(ExecutorService executorService, Callable... callables) {
        completionService = new ExecutorCompletionService<>(executorService);
        int order = 0;
        for(Callable callable : callables) {
            HashMap<Integer, Future<T>> task = new HashMap<>();
            task.put(order, completionService.submit(callable));
            taskList.add(task);
        }
    }
}
