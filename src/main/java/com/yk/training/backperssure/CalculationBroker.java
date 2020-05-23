package com.yk.training.backperssure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Accepts submitted {@link CalculationTask}.
 */
public class CalculationBroker {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationBroker.class);

    private static final int WORKERS_NUMBER = 10;

    private static final int SUBMITTED_TASKS_QUEUE_SIZE = 20;

    private final ExecutorService executorService = initializeThreadPoolWithRejection();
    private final Map<String, CalculationResult> calculationCache = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<CalculationResult>> submittedTasksCache = new ConcurrentHashMap<>();

    public CompletableFuture<CalculationResult> submit(final CalculationTask calculationTask) {
        final CalculationResult calculationResultCached = calculationCache.get(calculationTask.getName());
        // Check already calculated results.
        if (calculationResultCached != null) {
            return CompletableFuture.completedFuture(calculationResultCached);
        }

        // Check the cache of submitted tasks to prevent same task from being submitted again.
        final CompletableFuture<CalculationResult> calculationResult = submittedTasksCache.get(calculationTask.getName());
        if (calculationResult != null) {
            LOGGER.info("Rejecting a task {} because it was already submitted.", calculationTask.getName());
            return calculationResult;
        }

        LOGGER.info("Calculation submitted: {}.", calculationTask.getName());

        try {
            final CompletableFuture<CalculationResult> calculated = CompletableFuture
                    .supplyAsync(calculationTask::calculate, executorService);
            calculated.thenAccept(this::updateCalculatedResultsCache);
            calculated.thenAccept(this::updateSubmittedTasksCache);

            submittedTasksCache.put(calculationTask.getName(), calculated);
            return calculated;
        } catch (Exception e) {
            LOGGER.warn("Failed to submit a task: {}.", calculationTask.getName());
            return CompletableFuture.failedFuture(e);
        }
    }

    private void updateSubmittedTasksCache(final CalculationResult calculationResult) {
        submittedTasksCache.remove(calculationResult.getTaskName());
    }

    private void updateCalculatedResultsCache(final CalculationResult calculationResult) {
        calculationCache.put(calculationResult.getTaskName(), calculationResult);
    }

    public void close() {
        // No new tasks will be accepted.
        executorService.shutdown();
    }

    private ExecutorService initializeThreadPoolWithRejection() {
        final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        return new ThreadPoolExecutor(WORKERS_NUMBER, WORKERS_NUMBER,
                0L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(SUBMITTED_TASKS_QUEUE_SIZE),
                handler);
    }
}
