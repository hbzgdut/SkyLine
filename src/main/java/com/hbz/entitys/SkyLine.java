package com.hbz.entitys;

public class SkyLine implements Comparable<SkyLine>{
    private double x;// 天际线左端点x坐标
    private double y;// 天际线右端点y坐标
    private double len; // 天际线长度

    public SkyLine(double x, double y, double len) {
        this.x = x;
        this.y = y;
        this.len = len;
    }

    //排序规则，优先排y最小的，y一样，排x最小的
    @Override
    public int compareTo(SkyLine o) {
        int res = Double.compare(this.y, o.y);
        return res == 0 ? Double.compare(this.x, o.x) : res;
    }
    //重写toString，方便打印查看天际线
    @Override
    public String toString() {
        return "SkyLine{" +
                "x=" + x +
                ", y=" + y +
                ", len=" + len +
                '}';
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

    public double getLen() {
        return len;
    }

    public void setLen(double len) {
        this.len = len;
    }
}
