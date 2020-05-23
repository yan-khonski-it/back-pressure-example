package com.yk.training.backperssure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

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
        }

        calculationBroker.close();
        sleep();
        completableFutures.forEach(Main::completeFuture);
        System.out.println("Total tasks submitted: " + completableFutures.size());

        System.out.println("Total tasks finished: " + finishedTasks.get());
        System.out.println("Failed tasks: " + failedTasks.get());
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
            System.out.println("Task is finished: " + calculationResult);
            finishedTasks.incrementAndGet();
        } catch (InterruptedException e) {
            System.out.println("Task was interrupted. " + e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("Task failed.");
            failedTasks.incrementAndGet();
        }
    }
}
