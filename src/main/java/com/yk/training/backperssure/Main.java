package com.yk.training.backperssure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final AtomicInteger finishedTasks = new AtomicInteger();
    private static final AtomicInteger failedTasks = new AtomicInteger();

    public static void main(String[] args) {
        final int N_TASKS = 100;
        final CalculationBroker calculationBroker = new CalculationBroker();
        final List<CompletableFuture<CalculationResult>> completableFutures = new ArrayList<>();
        for (int i = 0; i < N_TASKS; i++) {
            final CalculationTask calculationTask = createCalculationTask(i);
            final CompletableFuture<CalculationResult> calculationResultCompletableFuture =
                    calculationBroker.submit(calculationTask);
            completableFutures.add(calculationResultCompletableFuture);
            if (i == 30) {
                sleep(3000);
            }

            if (i % 3 == 0) {
                final CalculationTask calculationTaskCopy = createCalculationTask(i);
                final CompletableFuture<CalculationResult> calculationResultCompletableFutureCopy =
                        calculationBroker.submit(calculationTaskCopy);
                completableFutures.add(calculationResultCompletableFutureCopy);
            }
        }

        calculationBroker.close();
        sleep();
        completableFutures.forEach(Main::completeFuture);
        LOGGER.info("Total tasks submitted: {}", completableFutures.size());

        LOGGER.info("Total tasks finished: {}.", finishedTasks.get());
        LOGGER.info("Failed tasks: {}.", failedTasks.get());
    }

    private static CalculationTask createCalculationTask(final int counter) {
        return new CalculationTask("CalculationTask_" + counter);
    }

    private static void sleep(final int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to sleep.", e);
        }
    }

    private static void sleep() {
        sleep(10000);
    }

    private static void completeFuture(final CompletableFuture<CalculationResult> future) {
        final CalculationResult calculationResult;
        try {
            calculationResult = future.get();
            LOGGER.info("Task is finished: {}.", calculationResult);
            finishedTasks.incrementAndGet();
        } catch (InterruptedException e) {
            LOGGER.error("Task was interrupted: {}.", e.getMessage());
        } catch (ExecutionException e) {
            LOGGER.error("Task failed.");
            failedTasks.incrementAndGet();
        }
    }
}
