package ru.ibs.framework.utils;

public enum DepositType {
    ALL_DEPOSITS("Все вклады"),
    ORDINARY_DEPOSITS("Обычные вклады"),
    PENSION("Пенсионный"),
    CHILDISH("Детский"),
    INVESTMENT("Инвестиционный"),
    SAVINGS_DEPOSIT("Накопительный счет");

    private  String type;

    public String getType() {
        return type;
    }

    DepositType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
