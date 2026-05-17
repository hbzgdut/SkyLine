package com.hbz.model;

import com.hbz.entitys.Item;
import com.hbz.entitys.PlaceItem;
import com.hbz.entitys.SkyLine;
import com.hbz.entitys.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class SkyLinePacking {
    private Double W;//放置框宽
    private Double H;//放置框高
    private Item[] items;//矩形数组
    private boolean isRotateEnable;//是否允许旋转
    // 基于堆优化的天际线优先队列（PriorityBlockingQueue是线程安全的，底层基于最小二叉堆实现，具有高效动态排序能力）
    private PriorityBlockingQueue<SkyLine> skyLineQueue = new PriorityBlockingQueue<>();

    public SkyLinePacking(boolean isRotateEnable, Double w, Double h, Item[] items) {
        this.isRotateEnable = isRotateEnable;
        W = w;
        H = h;
        this.items = items;
    }

    public Solution packing(){
        //记录放置结果
        List<PlaceItem> placeItemList = new ArrayList<>();
        //记录放置总面积
        double area = 0;
        //添加初始天际线
        skyLineQueue.add(new SkyLine(0.0, 0.0, W));
        //判断矩形是否被放置数组
        boolean[] isPlaced = new boolean[items.length];
        //开始天际线启发式迭代
        while (!skyLineQueue.isEmpty() && placeItemList.size()<items.length) {
            SkyLine poll = skyLineQueue.poll();//获取天际线当前最下最左的天际线
            //初始化hl和hr
            double hl = H-poll.getY();
            double hr = H-poll.getY();
            //提前跳出计算器(如果hl和hr都获得了就跳出，节省时间)
            int count = 0;
            //1.顺序遍历天际线，根据天际线信息计算hl和hr
            for (SkyLine line : skyLineQueue) {
                if(compareDouble(line.getX()+line.getLen(),poll.getX()) == 0){
                    //尾头相连，是hl
                    hl = line.getY() - poll.getY();
                    count++;
                } else if (compareDouble(line.getX(),poll.getX()+poll.getLen()) == 0) {
                    //头尾相连，是hr
                    hr = line.getY() - poll.getY();
                    count++;
                }
                if(count == 2){
                    //找到两个相连天际线，跳出
                    break;
                }
            }
            //2.遍历矩形，计算每个矩形的得分情况
            int maxItemIndex = -1;//最大得分矩形索引
            boolean isRotate = false;//是否旋转
            int maxScore = -1;//最大得分
            for (int i = 0; i < items.length; i++){
                //获取未使用的矩形
                if(!isPlaced[i]){
                    int score = getScore(items[i].getW(), items[i].getH(), poll, hl, hr);
                    if (score > maxScore){
                        maxScore = score;
                        maxItemIndex = i;
                        isRotate = false;
                    }//旋转后的分
                    if (isRotateEnable && items[i].getH() != items[i].getW()){
                        int spinScore = getScore(items[i].getH(), items[i].getW(), poll, hl, hr);
                        if (spinScore > maxScore){
                            maxScore = spinScore;
                            maxItemIndex = i;
                            isRotate = true;
                        }
                    }
                }
            }
            //3.将最大的的分矩形放置到placeItemList中
            if(maxScore >= 0){
                //左墙低于右墙
                if(hl<hr){
                    //如果得分为4或者0，矩形靠天际线右边放，否则靠左放
                    if(maxScore == 4 || maxScore == 0){
                        placeItemList.add(placeRight(items[maxItemIndex],poll,isRotate));
                    }else {
                        placeItemList.add(placeLeft(items[maxItemIndex],poll,isRotate));
                    }
                }else {
                    //右墙低于左墙
                    //如果得分为2，放靠右放，否则靠左放
                    if(maxScore == 2){
                        placeItemList.add(placeRight(items[maxItemIndex],poll,isRotate));
                    }else {
                        placeItemList.add(placeLeft(items[maxItemIndex],poll,isRotate));
                    }
                }
                isPlaced[maxItemIndex] = true;
                area += items[maxItemIndex].getW()*items[maxItemIndex].getH();
            }else {
                //如果当前天际线没有得分矩形，说明没有一个得分矩形，放置失败
                //与其它天际线合并成一条新的天际线
                mergeSkylines(poll);
            }
        }
        double rate = area/(W*H);
        return new Solution(placeItemList,rate,area);
    }

    private void mergeSkylines(SkyLine poll) {
        boolean canMerge = false;
        //只能合并比poll高的天际线
        for (SkyLine line : skyLineQueue) {
            if(compareDouble(poll.getY(),line.getY())!=1){
                //poll的头跟line尾相连
                if(compareDouble(poll.getX(), line.getX()+line.getLen()) == 0){
                    skyLineQueue.remove(line);
                    canMerge = true;
                    poll.setX(line.getX());
                    poll.setY(line.getY());
                    poll.setLen(line.getLen()+poll.getLen());
                    break;
                }
                //poll的尾跟line头相连
                if(compareDouble(poll.getX()+poll.getLen(), line.getX()) == 0){
                    skyLineQueue.remove(line);
                    canMerge = true;
                    poll.setY(line.getY());
                    poll.setLen(line.getLen()+poll.getLen());
                    break;
                }
            }
        }
        //合并天际线
        if(canMerge){
            //原poll已经被弹出去，现在的poll是合并后的新天际线
            skyLineQueue.add(poll);
        }
    }

    private PlaceItem placeLeft(Item item, SkyLine poll, boolean isRotate) {
        PlaceItem placeItem = null;
        if (isRotate){
            placeItem = new PlaceItem(poll.getX(), poll.getY(), item.getH(), item.getW(), true, item.getName());
        }else{
            placeItem = new PlaceItem(poll.getX(), poll.getY(), item.getW(), item.getH(), false, item.getName());
        }
        //添加天际线
        addSkyLine(poll.getX(), poll.getY()+placeItem.getH(),placeItem.getW());
        addSkyLine(poll.getX()+placeItem.getW(), poll.getY(), poll.getLen()-placeItem.getW());
        return placeItem;
    }

    private PlaceItem placeRight(Item item, SkyLine poll, boolean isRotate) {
        PlaceItem placeItem = null;
        if (isRotate){
            placeItem = new PlaceItem((poll.getX()+poll.getLen()-item.getH()), poll.getY(), item.getH(), item.getW(), true, item.getName());
        }else{
            placeItem = new PlaceItem((poll.getX()+poll.getLen()-item.getW()), poll.getY(), item.getW(), item.getH(), false, item.getName());
        }
        //添加天际线
        addSkyLine(poll.getX(), poll.getY(), poll.getLen()-placeItem.getW());
        addSkyLine(placeItem.getX(), poll.getY()+placeItem.getH(), placeItem.getW());
        return placeItem;
    }

    private void addSkyLine(double x, double y, double len) {
        if(compareDouble(len,0.0) == 1){
            skyLineQueue.add(new SkyLine(x, y, len));
        }
    }

    private int getScore(double w, double h, SkyLine poll, double hl, double hr) {
        //天际线宽度放不下矩形
        if(compareDouble(poll.getLen(),w) == -1){
            return -1;
        }
        //超出放置框上界
        if(compareDouble(H, poll.getY()+h) == -1){
            return -1;
        }
        int score = -1;
        //左墙高度大于等于右墙
        if (hl>=hr){
            if(compareDouble(w, poll.getLen()) == 0 && compareDouble(h, hl) == 0){
                score = 7;
            } else if (compareDouble(w,poll.getLen()) == 0 && compareDouble(h,hr) == 0) {
                score = 6;
            } else if (compareDouble(w, poll.getLen()) == 0 && compareDouble(h,hl) == 1) {
                score = 5;
            } else if (compareDouble(w, poll.getLen()) == -1 && compareDouble(h,hl) == 0) {
                score = 4;
            } else if (compareDouble(w, poll.getLen()) == 0 && compareDouble(hr,h) == -1 && compareDouble(hl,h) == 1) {
                score = 3;
            } else if (compareDouble(w, poll.getLen()) == -1 && compareDouble(hr,h) == 0) {
                score = 2;
            } else if (compareDouble(w, poll.getLen()) == 0 && compareDouble(hr,h) == 1) {
                score = 1;
            }else if(compareDouble(w, poll.getLen()) == -1 && compareDouble(hl,h) != 0){
                return 0;
            }else {
                throw new RuntimeException("w="+w+" | h="+h+" | poll="+poll+" | hl="+hl+" | hr="+hr);
            }//左墙高度小于右墙
        }else {
            if(compareDouble(w,poll.getLen()) == 0 && compareDouble(h,hr) == 0){
                score = 7;
            } else if (compareDouble(w,poll.getLen()) == 0 && compareDouble(h,hl) == 0) {
                score = 6;
            } else if (compareDouble(w,poll.getLen()) == 0 && compareDouble(h,hr) == 1) {
                score = 5;
            }else if(compareDouble(w, poll.getLen()) == -1 && compareDouble(h,hr) == 0){
                score = 4;
            } else if (compareDouble(w,poll.getLen()) == 0 && compareDouble(hl,h) == -1 && compareDouble(h,hr) == -1) {
                score = 3;
            } else if (compareDouble(w, poll.getLen()) == -1 && compareDouble(h,hl) == 0) {
                score = 2;
            } else if (compareDouble(w,poll.getLen()) == 0 && compareDouble(h,hl) == -1) {
                score = 1;
            } else if (compareDouble(w, poll.getLen()) == -1 && compareDouble(hr,h) !=0) {
                score = 0;
            }else {
                throw new RuntimeException("w="+w+" | h="+h+" | poll="+poll+" | hl="+hl+" | hr="+hr);
            }
        }
        return score;
    }

    private int compareDouble(double d1, double d2) {
        double error = 1e-6;
        if (Math.abs(d1 - d2) < error) {
            return 0;
        } else if (d1 > d2) {
            return 1;
        } else if(d1 < d2){
            return -1;
        }else {
            throw new RuntimeException("d1="+d1+" | d2="+d2);
        }
    }
}
