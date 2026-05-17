package com.hbz.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;



/*
 *实例类用来记录放置框大小，是否允许旋转，放置矩形的列表
 */

public class Instance {
    //放置框的宽
    private int width;
    //放置框的高
    private int height;
    //放置矩形的列表，用于存放全部矩形
    private List<Item> itemsList;
    //是否允许旋转
    private boolean isRotateEnable;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public boolean isRotateEnable() {
        return isRotateEnable;
    }

    public void setRotateEnable(boolean rotateEnable) {
        isRotateEnable = rotateEnable;
    }
}
