package com.app.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

public class Deal implements Serializable {

    private final int ID;
    private final String currencyName;
    private final double currencyPurchasePriceInUSDT;
    private double purchaseAmount, currencySalePriceInUSDT, currencyQuantity;
    private boolean isFinished;
    private final Date timeStarted;
    private final String startDate;
    private String endDate;
    String profitRatio;
    private final HashMap<String, Double> profitGoals, lossGoals;

    public Deal(int ID, String currencyName, double currencyPurchasePriceInUSDT, double purchaseAmount) {
        this.ID = ID;
        this.currencyName = currencyName;
        this.currencyPurchasePriceInUSDT = currencyPurchasePriceInUSDT;
        this.currencyQuantity = formatNumber(purchaseAmount / currencyPurchasePriceInUSDT, 8);
        this.currencySalePriceInUSDT = 0.0;
        this.purchaseAmount = formatNumber(purchaseAmount, 2);
        this.profitRatio = "%0";
        this.isFinished = false;
        this.timeStarted = new Date();
        this.startDate = generateCurrentDateFormatted();
        this.endDate = "Null";
        this.profitGoals = new HashMap<>();
        this.lossGoals = new HashMap<>();
        makeProfitGoals();
        makeLossGoals();
    }

    private void makeProfitGoals() {
        profitGoals.put("1%", formatNumber(currencyPurchasePriceInUSDT * 1.01, 8));
        profitGoals.put("3%", formatNumber(currencyPurchasePriceInUSDT * 1.03, 8));
        profitGoals.put("5%", formatNumber(currencyPurchasePriceInUSDT * 1.05, 8));
        profitGoals.put("10%", formatNumber(currencyPurchasePriceInUSDT * 1.1, 8));
        profitGoals.put("15%", formatNumber(currencyPurchasePriceInUSDT * 1.15, 8));
        profitGoals.put("20%", formatNumber(currencyPurchasePriceInUSDT * 1.2, 8));
        profitGoals.put("25%", formatNumber(currencyPurchasePriceInUSDT * 1.25, 8));
        profitGoals.put("30%", formatNumber(currencyPurchasePriceInUSDT * 1.3, 8));
        profitGoals.put("35%", formatNumber(currencyPurchasePriceInUSDT * 1.35, 8));
        profitGoals.put("40%", formatNumber(currencyPurchasePriceInUSDT * 1.4, 8));
    }

    private void makeLossGoals() {
        lossGoals.put("-1%", formatNumber(currencyPurchasePriceInUSDT * 0.99, 8));
        lossGoals.put("-3%", formatNumber(currencyPurchasePriceInUSDT * 0.97, 8));
        lossGoals.put("-5%", formatNumber(currencyPurchasePriceInUSDT * 0.95, 8));
        lossGoals.put("-10%", formatNumber(currencyPurchasePriceInUSDT * 0.9, 8));
        lossGoals.put("-15%", formatNumber(currencyPurchasePriceInUSDT * 0.85, 8));
        lossGoals.put("-20%", formatNumber(currencyPurchasePriceInUSDT * 0.8, 8));
        lossGoals.put("-25%", formatNumber(currencyPurchasePriceInUSDT * 0.75, 8));
        lossGoals.put("-30%", formatNumber(currencyPurchasePriceInUSDT * 0.7, 8));
        lossGoals.put("-35%", formatNumber(currencyPurchasePriceInUSDT * 0.65, 8));
        lossGoals.put("-40%", formatNumber(currencyPurchasePriceInUSDT * 0.6, 8));
    }

    private String generateCurrentDateFormatted() {
        String[] date = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(timeStarted).split(" ");
        String day = date[0];
        String[] time = date[1].split(":");
        int hours = Integer.parseInt(time[0]);
        String amOrPm = "AM";
        if (hours > 12) {
            amOrPm = "PM";
            hours -= 12;
        }

        return day + " " + hours + ":" + time[1] + ":" + time[2] + " " + amOrPm;
    }

    private double formatNumber(double value, int fractions) {
        String format = "%." + fractions + 'f';
        String formatted = new Formatter().format(format, value).toString();
        return Double.parseDouble(formatted);
    }

    public double finish(double currencySalePriceInUSDT) {
        this.currencySalePriceInUSDT = currencySalePriceInUSDT;
        endDate = generateCurrentDateFormatted();
        isFinished = true;
        double profits = formatNumber(((currencySalePriceInUSDT - currencyPurchasePriceInUSDT) / currencyPurchasePriceInUSDT) * 100, 2);
        profitRatio = profits + "%";
        return formatNumber(currencyQuantity * currencySalePriceInUSDT, 2);
    }

    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = formatNumber(purchaseAmount, 2);
    }

    public void setCurrencyQuantity(double currencyQuantity) {
        this.currencyQuantity = formatNumber(currencyQuantity, 8);
    }

    public boolean isGainer() {
        return currencyPurchasePriceInUSDT < currencySalePriceInUSDT;
    }

    public boolean isLoser() {
        return currencyPurchasePriceInUSDT > currencySalePriceInUSDT;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public double getCurrencyPurchasePriceInUSDT() {
        return currencyPurchasePriceInUSDT;
    }

    public double getPurchaseAmount() {
        return purchaseAmount;
    }

    public double getCurrencySalePriceInUSDT() {
        return currencySalePriceInUSDT;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public double getCurrencyQuantity() {
        return currencyQuantity;
    }

    public int getID() {
        return ID;
    }

    public String getProfitRatio() {
        return profitRatio;
    }

    public HashMap<String, Double> getLossGoals() {
        return lossGoals;
    }

    public HashMap<String, Double> getProfitGoals() {
        return profitGoals;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "ID=" + ID +
                ", currencyName='" + currencyName + '\'' +
                ", currencyPurchasePriceInUSDT=" + currencyPurchasePriceInUSDT +
                ", purchaseAmount=" + purchaseAmount +
                ", currencySalePriceInUSDT=" + currencySalePriceInUSDT +
                ", currencyQuantity=" + currencyQuantity +
                ", isFinished=" + isFinished +
                ", timeStarted=" + timeStarted +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", profitRatio='" + profitRatio + '\'' +
                ", profitGoals=" + profitGoals +
                ", lossGoals=" + lossGoals +
                '}';
    }
}
