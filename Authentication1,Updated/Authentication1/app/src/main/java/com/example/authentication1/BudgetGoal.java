package com.example.authentication1;

public class BudgetGoal {
    private double minGoal;
    private double maxGoal;
    private String lastUpdated;

    // Default constructor required for Firebase DataSnapshot
    public BudgetGoal() {
    }

    public BudgetGoal(double minGoal, double maxGoal, String lastUpdated) {
        this.minGoal = minGoal;
        this.maxGoal = maxGoal;
        this.lastUpdated = lastUpdated;
    }

    public double getMinGoal() { return minGoal; }
    public void setMinGoal(double minGoal) { this.minGoal = minGoal; }

    public double getMaxGoal() { return maxGoal; }
    public void setMaxGoal(double maxGoal) { this.maxGoal = maxGoal; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
}