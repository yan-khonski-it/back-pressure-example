package com.yk.training.backperssure;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Accepts submitted {@link CalculationTask}.
 */
public class CalculationBroker {

    private static final int MAX_WORKERS_NUMBER = 5;

    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_WORKERS_NUMBER);
    private final Map<String, CalculationResult> calculationCache = new ConcurrentHashMap<>();

    public CompletableFuture<CalculationResult> submit(final CalculationTask calculationTask) {
        final CalculationResult calculationResultCached = calculationCache.get(calculationTask.getName());
        if (calculationResultCached != null) {
            return CompletableFuture.completedFuture(calculationResultCached);
        }

        System.out.println("Calculation submitted: " + calculationTask.getName());

        final CompletableFuture<CalculationResult> calculated = CompletableFuture
                .supplyAsync(calculationTask::calculate, executorService);
        calculated.thenAccept(this::updateCache);
        return calculated;
    }

    private void updateCache(final CalculationResult calculationResult) {
        calculationCache.put(calculationResult.getTaskName(), calculationResult);
    }

    public void close() {
        // No new tasks will be accepted.
        executorService.shutdown();
    }
}
