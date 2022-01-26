package com.loamoa.loamoa.domain;

public class Item {
    Long id, Price, yesterdayPrice;
    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long price) {
        Price = price;
    }

    public Long getYesterdayPrice() {
        return yesterdayPrice;
    }

    public void setYesterdayPrice(Long yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
