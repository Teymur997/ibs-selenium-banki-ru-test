package ru.ibs.framework.utils;

public class DepositParameters {

    private String depositValue;
    private String depositPeriod;
    private String type;
    private String[] banks;
    private String[] additionals;

    public String getDepositValue() {
        return depositValue;
    }


    public String getDepositPeriod() {
        return depositPeriod;
    }

    public String[] getBanks() {
        return banks;
    }

    public String getType() {
        return type;
    }

    public String[] getAdditionals() {
        return additionals;
    }

    public DepositParameters(String depositValue, String depositPeriod, String type, String[] banks, String[] additionals) {
        this.depositValue = depositValue;
        this.depositPeriod = depositPeriod;
        this.type = type;
        this.banks = banks;
        this.additionals = additionals;
    }
}
