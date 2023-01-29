package ru.ibs.framework.utils;

public enum DepositPeriod {
    ANY("любой"),
    ONE_MONTH("1 месяц"),
    THREE_MONTH("3 месяца"),
    SIX_MONTH("6 месяцев"),
    NINE_MONTH("9 месяцев"),
    ONE_YEAR("1 год"),
    A_YEAR_AND_A_HALF("1,5 года"),
    TWO_YEARS("2 года"),
    THREE_YEARS("3 года"),
    FOUR_YEARS("4 года"),
    FIVE_YEARS("5 лет");

    private String period;

    public String getPeriod() {
        return period;
    }

    DepositPeriod(String period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return period;
    }
}
