package com.apiflow.infrastructure.async;

import com.apiflow.api.async.AsyncGateway;
import com.apiflow.common.util.TraceContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Component
public class VirtualThreadAsyncGateway implements AsyncGateway {

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(TraceContext.wrap(supplier), executor);
    }

    @Override
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(TraceContext.wrap(runnable), executor);
    }
}
