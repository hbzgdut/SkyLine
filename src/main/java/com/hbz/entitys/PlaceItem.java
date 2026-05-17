package com.hbz.entitys;
/*
 * 放置矩形类
 * 已放置矩形的坐标信息，旋转信息，宽高信息
 */
public class PlaceItem {
    private double x,y;// 放置坐标
    private double w,h;// 矩形宽高
    private boolean isRotate;// 是否旋转
    private String name;// 名字
    // 构造函数
    public PlaceItem(double x, double y, double w, double h, boolean isRotate, String name) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isRotate = isRotate;
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public boolean isRotate() {
        return isRotate;
    }

    public void setRotate(boolean rotate) {
        isRotate = rotate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
