package ru.ibs.framework.utils;

public class Deposit {
    private String bankName;

    private String rate;

    private String period;

    private String profit;

    public Deposit(String bankName, String rate, String period, String profit) {
        this.bankName = bankName;
        this.rate = rate;
        this.period = period;
        this.profit = profit;
    }

    public String getBankName() {
        return bankName;
    }

    public String getRate() {
        return rate;
    }

    public String getPeriod() {
        return period;
    }

    public String getProfit() {
        return profit;
    }

    @Override
    public String toString() {
        return bankName;
    }
}