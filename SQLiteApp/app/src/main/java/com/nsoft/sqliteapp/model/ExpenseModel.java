package com.nsoft.sqliteapp.model;

public class ExpenseModel {
    private String type;
    private int id;
    private double amount;
    private String reason;
    private long time;

    public ExpenseModel(String type, int id, double amount, String reason, long time) {
        this.type = type;
        this.id = id;
        this.amount = amount;
        this.reason = reason;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
