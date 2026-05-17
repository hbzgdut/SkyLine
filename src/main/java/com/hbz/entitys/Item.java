package com.hbz.entitys;
/**
 * 矩形类
 * 记录每个矩形的宽，高，名字信息
 */
public class Item {
    private double w;// 矩形宽
    private double h;// 矩形高
    private String name;// 名字

    // 构造函数
    public Item(double w, double h, String name) {
        this.w = w;
        this.h = h;
        this.name = name;
    }

    // 复制单个矩形
    public static Item copy(Item item) {
        return new Item(item.w, item.h, item.name);
    }
    // 复制矩形列表
    public static Item[] copy(Item[] items) {
        Item[] newItems = new Item[items.length];
        for (int i = 0; i < items.length; i++) {
            newItems[i] = Item.copy(items[i]);
        }
        return newItems;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
