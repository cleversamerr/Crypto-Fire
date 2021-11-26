package com.app.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedList;

public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name, username, password;
    private double balanceInUSDT;
    private final LinkedList<Report> reports;
    private final LinkedList<Deal> activeDeals;
    private final LinkedList<Deal> finishedDeals;
    private final LinkedList<Currency> currencies;
    private int dealsNumber;

    public Account(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.balanceInUSDT = 0;
        this.dealsNumber = 0;
        this.reports = new LinkedList<>();
        this.activeDeals = new LinkedList<>();
        this.finishedDeals = new LinkedList<>();
        this.currencies = new LinkedList<>();
    }

    public void addCurrency(String currencyName, double currencyQuantity) {
        currencies.addLast(new Currency(currencyName, currencyQuantity));
    }

    public void removeCurrency(String currencyName) {
        currencies.removeIf(currency -> currency.getCurrencyName().equals(currencyName));
    }

    public void changeName(String name) {
        if (name.length() >= 3 && name.length() <= 16)
            this.name = name;
    }

    public void changeUsername(String username) {
        if (username.length() >= 3 && username.length() <= 16)
            this.username = username;
    }

    public void changePassword(String password) {
        if (password.length() >= 8 && password.length() <= 16)
            this.password = password;
    }

    private void addDeal(String currencyName, double currencyInUSDT, double purchaseAmountInUSDT) {
        activeDeals.addFirst(new Deal(++dealsNumber, currencyName, currencyInUSDT, purchaseAmountInUSDT));
    }

    private void addReport(String title, String description) {
        reports.addFirst(new Report(title, description));
    }

    public void depositUSDT(double amount) {
        this.balanceInUSDT += amount < 10 ? 0 : amount;
    }

    public void withdrawUSDT(double amount) {
        this.balanceInUSDT -= amount >= 10 && amount <= balanceInUSDT ? amount : 0;
    }

    public void makeDeal(String currencyName, double currencyInUSDT, double purchaseAmountInUSDT) {
        if (purchaseAmountInUSDT > balanceInUSDT || purchaseAmountInUSDT < 10) return;

        Currency currency = findCurrency(currencyName);
        double currencyQuantity = purchaseAmountInUSDT / currencyInUSDT;
        if (currency == null)
            addCurrency(currencyName, currencyQuantity);
        else
            currency.deposit(currencyQuantity);

        addDeal(currencyName, currencyInUSDT, purchaseAmountInUSDT);
        addReport("Purchase", "You purchased " + formatNumber(currencyQuantity, 2) + ' ' + currencyName + " with " + formatNumber(purchaseAmountInUSDT, 2) + " USDT.");
        withdrawUSDT(purchaseAmountInUSDT);
    }

    private double formatNumber(double value, int fractions) {
        String format = "%." + fractions + 'f';
        String formatted = new Formatter().format(format, value).toString();
        return Double.parseDouble(formatted);
    }

    public void finishDeal(int dealID, double currencySalePriceInUSDT, double currencyQuantityToSale) {
        Deal deal = findDeal(dealID);

        double remainingAmountOfCurrency = deal.getCurrencyQuantity() - currencyQuantityToSale;
        if (remainingAmountOfCurrency != 0) {
            String currencyName = deal.getCurrencyName();
            double currencyPurchasePriceInUSDT = deal.getCurrencyPurchasePriceInUSDT();
            double purchaseAmount = remainingAmountOfCurrency * currencyPurchasePriceInUSDT;
            activeDeals.addFirst(new Deal(++dealsNumber, currencyName, currencyPurchasePriceInUSDT, purchaseAmount));

            deal.setCurrencyQuantity(currencyQuantityToSale);
            deal.setPurchaseAmount(currencyPurchasePriceInUSDT * currencyQuantityToSale);

            Currency currency = findCurrency(deal.getCurrencyName());
            currency.withdraw(currencyQuantityToSale);
        }

        double finalAmountOfDealInUSDT = deal.finish(currencySalePriceInUSDT);
        depositUSDT(finalAmountOfDealInUSDT);
        addReport("Selling", "You sold " + formatNumber(currencyQuantityToSale, 2) + ' ' + deal.getCurrencyName() + " with " + formatNumber(finalAmountOfDealInUSDT, 2) + " USDT.");
        activeDeals.remove(deal);
        finishedDeals.addFirst(deal);
        currencies.removeIf(currency -> remainingAmountOfCurrency == 0);
    }

    public Deal findDeal(int dealID) {
        if (dealID < 1 || dealID > dealsNumber)
            return null;

        for (Deal deal : activeDeals)
            if (deal.getID() == dealID)
                return deal;

        return null;
    }

    public Currency findCurrency(String currencyName) {
        for (Currency currency : currencies)
            if (currency.getCurrencyName().equals(currencyName))
                return currency;

        return null;
    }

    public LinkedList<Deal> getCurrencyDeals(String currencyName) {
        LinkedList<Deal> result = new LinkedList<>();
        for (Deal deal : activeDeals)
            if (deal.getCurrencyName().equals(currencyName))
                result.addLast(deal);

        return result.isEmpty() ? null : result;
    }

    public LinkedList<Deal> getFinishedDeals() {
        return finishedDeals;
    }

    public LinkedList<Deal> getActiveDeals() {
        return activeDeals;
    }

    public LinkedList<Report> getReports() {
        return reports;
    }

    public LinkedList<Deal> getGainerDealsLastDay() {
        Date currentDate = new Date();
        LinkedList<Deal> result = new LinkedList<>();
        for (Deal deal : finishedDeals) {
            long diffInMins = (currentDate.getTime() - deal.getTimeStarted().getTime()) / 1000 / 60;

            if (diffInMins > 1440)
                break;

            if (deal.isGainer())
                result.addLast(deal);
        }

        return result;
    }

    public LinkedList<Deal> getGainerDealsLastWeek() {
        Date currentDate = new Date();
        LinkedList<Deal> result = new LinkedList<>();
        for (Deal deal : finishedDeals) {
            long diffInDays = (currentDate.getTime() - deal.getTimeStarted().getTime()) / 1000 / 60 / 1440;

            if (diffInDays > 7)
                break;

            if (deal.isGainer())
                result.addLast(deal);
        }

        return result;
    }

    public LinkedList<Deal> getGainerDealsLastMonth() {
        Date currentDate = new Date();
        LinkedList<Deal> result = new LinkedList<>();
        for (Deal deal : finishedDeals) {
            long diffInDays = (currentDate.getTime() - deal.getTimeStarted().getTime()) / 1000 / 60 / 1440;

            if (diffInDays > 30)
                break;

            if (deal.isGainer())
                result.addLast(deal);
        }

        return result;
    }

    public LinkedList<Deal> getLoserDealsLastDay() {
        Date currentDate = new Date();
        LinkedList<Deal> result = new LinkedList<>();
        for (Deal deal : finishedDeals) {
            long diffInMins = (currentDate.getTime() - deal.getTimeStarted().getTime()) / 1000 / 60;

            if (diffInMins > 1440)
                break;

            if (deal.isLoser())
                result.addLast(deal);
        }

        return result;
    }

    public LinkedList<Deal> getLoserDealsLastWeek() {
        Date currentDate = new Date();
        LinkedList<Deal> result = new LinkedList<>();
        for (Deal deal : finishedDeals) {
            long diffInDays = (currentDate.getTime() - deal.getTimeStarted().getTime()) / 1000 / 60 / 1440;

            if (diffInDays > 7)
                break;

            if (deal.isLoser())
                result.addLast(deal);
        }

        return result;
    }

    public LinkedList<Deal> getLoserDealsLastMonth() {
        Date currentDate = new Date();
        LinkedList<Deal> result = new LinkedList<>();
        for (Deal deal : finishedDeals) {
            long diffInDays = (currentDate.getTime() - deal.getTimeStarted().getTime()) / 1000 / 60 / 1440;

            if (diffInDays > 30)
                break;

            if (deal.isLoser())
                result.addLast(deal);
        }

        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balanceInUSDT=" + balanceInUSDT +
                ", reports=" + reports +
                ", activeDeals=" + activeDeals +
                ", finishedDeals=" + finishedDeals +
                ", currencies=" + currencies +
                ", dealsNumber=" + dealsNumber +
                '}';
    }
}
