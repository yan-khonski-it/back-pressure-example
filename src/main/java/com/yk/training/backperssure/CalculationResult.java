package com.yk.training.backperssure;

public class CalculationResult {

    private final String taskName;
    private final String text;
    private final Integer number;
    private final Long durationMs;

    public CalculationResult(final String taskName, final String text, final Integer number, final Long durationMs) {
        this.taskName = taskName;
        this.text = text;
        this.number = number;
        this.durationMs = durationMs;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getText() {
        return text;
    }

    public Integer getNumber() {
        return number;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    @Override
    public String toString() {
        return "CalculationResult{" +
                "taskName='" + taskName + '\'' +
                ", text='" + text + '\'' +
                ", number=" + number +
                ", durationMs=" + durationMs +
                '}';
    }
}
