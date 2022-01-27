package com.loamoa.loamoa.domain;

public class Item {
    float recentTradingPrice, currentMinPrice, dailyTradingPrice;
    String name;

    public float getRecentTradingPrice() {
        return recentTradingPrice;
    }

    public void setRecentTradingPrice(float recentTradingPrice) {
        this.recentTradingPrice = recentTradingPrice;
    }

    public float getCurrentMinPrice() {
        return currentMinPrice;
    }

    public void setCurrentMinPrice(float currentMinPrice) {
        this.currentMinPrice = currentMinPrice;
    }

    public float getDailyTradingPrice() {
        return dailyTradingPrice;
    }

    public void setDailyTradingPrice(float dailyTradingPrice) {
        this.dailyTradingPrice = dailyTradingPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "recentTradingPrice=" + recentTradingPrice +
                ", currentMinPrice=" + currentMinPrice +
                ", dailyTradingPrice=" + dailyTradingPrice +
                ", name='" + name + '\'' +
                '}';
    }
}
