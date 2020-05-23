package com.yk.training.backperssure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Simulates a task calculation that takes some time.
 */
public class CalculationTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationTask.class);

    private static final Random RANDOM = new Random();
    private static final int MAX_WAIT_TIME_MS = 3000;
    private static final int MIN_WAIT_TIME_MS = 1000;

    private final String name;

    public CalculationTask(final String name) {
        this.name = name;
    }

    public CalculationResult calculate() {
        final long start = System.currentTimeMillis();
        final long waitTimeMs = MIN_WAIT_TIME_MS + RANDOM.nextInt(MAX_WAIT_TIME_MS);
        sleep(waitTimeMs);
        final int result = Math.abs(RANDOM.nextInt()) % 100;
        final String text = "This is result: " + result;
        final long end = System.currentTimeMillis();
        final long total = end - start;
        final CalculationResult calculationResult = new CalculationResult(name, text, result, total);
        LOGGER.info("Calculation finished: {}", calculationResult);
        return calculationResult;
    }

    public String getName() {
        return name;
    }

    private void sleep(final long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to sleep.");
        }
    }
}
