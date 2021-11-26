package com.app.models;

import java.io.Serializable;
import java.util.Formatter;

public class Currency implements Serializable {

    private final String currencyName;
    private double currencyQuantity;

    public Currency(String currencyName, double currencyQuantity) {
        this.currencyName = currencyName.toUpperCase();
        this.currencyQuantity = formatNumber(currencyQuantity);
    }

    public void deposit(double quantity) {
        currencyQuantity = formatNumber(currencyQuantity + quantity);
    }

    public void withdraw(double quantity) {
        currencyQuantity = formatNumber(currencyQuantity - quantity);
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public double getCurrencyQuantity() {
        return currencyQuantity;
    }

    private double formatNumber(double value) {
        String formatted = new Formatter().format("%.8f", value).toString();
        return Double.parseDouble(formatted);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currencyName='" + currencyName + '\'' +
                ", currencyQuantity=" + currencyQuantity +
                '}';
    }
}
