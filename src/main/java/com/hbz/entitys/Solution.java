package com.hbz.entitys;

import java.util.List;

public class Solution {
    // 放置结果
    private List<PlaceItem> placeItems;
    // 利用率
    private double rate;
    // 放置总面积
    private double area;

    public Solution(List<PlaceItem> placeItems, double rate, double area) {
        this.placeItems = placeItems;
        this.rate = rate;
        this.area = area;
    }

    public List<PlaceItem> getPlaceItems() {
        return placeItems;
    }

    public void setPlaceItems(List<PlaceItem> placeItems) {
        this.placeItems = placeItems;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
