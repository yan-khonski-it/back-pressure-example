package com.yk.training.backperssure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        final int N_TASKS = 100;
        final CalculationBroker calculationBroker = new CalculationBroker();
        final List<CompletableFuture<CalculationResult>> completableFutures = new ArrayList<>();
        for (int i = 0; i < N_TASKS; i++) {
            final CalculationTask calculationTask = createCalculationTask(i);
            final CompletableFuture<CalculationResult> calculationResultCompletableFuture =
                    calculationBroker.submit(calculationTask);
            completableFutures.add(calculationResultCompletableFuture);
        }

        calculationBroker.close();
    }

    private static CalculationTask createCalculationTask(final int counter) {
        return new CalculationTask("CalculationTask_" + counter);
    }
}
