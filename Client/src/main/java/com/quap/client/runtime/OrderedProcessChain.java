package com.quap.client.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class OrderedProcessChain  <R, T>{
    private final List<HashMap<Integer, Future<T>>> taskList = new ArrayList<>();
    private final CompletionService<T> completionService;

    public OrderedProcessChain(ExecutorService executorService, Callable... callables) {
        completionService = new ExecutorCompletionService<>(executorService);
        int order = 0;
        for(Callable callable : callables) {
            HashMap<Integer, Future<T>> task = new HashMap<>();
            task.put(order, completionService.submit(callable));
            taskList.add(task);
        }
    }

    public R process(HashMap<Integer, Future<T>>... tasks) {
        return null;
    }
}
