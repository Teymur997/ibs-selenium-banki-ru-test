package ru.ibs.framework.utils;

public class Parameters {
    DepositParameters depositParameters;
    String count;
    Deposit deposit;

    public DepositParameters getDepositParameters() {
        return depositParameters;
    }

    public String getCount() {
        return count;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public Parameters(DepositParameters depositParameters, String count, Deposit deposit) {
        this.depositParameters = depositParameters;
        this.count = count;
        this.deposit = deposit;
    }
}
