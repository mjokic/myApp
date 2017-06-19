package com.example.wifi.myapp.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Auction implements Serializable {

    private long id;
    public double startPrice;
    public Date startDate;
    public Date endDate;
    public User user;
    public Item item;
    public List<Bid> bids;
    private boolean over;

    public Auction(long id, double startPrice, Date endDate,
                   User user, Item item, boolean over) {
        this.id = id;
        this.startPrice = startPrice;
        this.startDate = new Date();
        this.endDate = endDate;
        this.user = user;
        this.item = item;
        this.bids = new ArrayList<>();
        this.over = over;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", startPrice=" + startPrice +
                ", item=" + item.getName();
    }
}
