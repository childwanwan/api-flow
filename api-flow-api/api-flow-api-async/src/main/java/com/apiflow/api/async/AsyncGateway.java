package com.apiflow.api.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface AsyncGateway {

    <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier);

    CompletableFuture<Void> runAsync(Runnable runnable);
}
