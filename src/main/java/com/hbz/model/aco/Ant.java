package com.hbz.model.aco;

import com.hbz.entitys.Item;
import com.hbz.entitys.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant {
    //矩形集合
    private Item[] items;
    //已经放置的矩形索引
    private List<Integer> sequence;
    //还未放置的矩形索引
    private List<Integer> allowedItems;
    //信息素变化矩阵
    private double[][] delta;
    //矩形不同度矩阵
    private double[][] different;
    //信息素重要度
    private double alpha;
    //启发式因子重要程度
    private double beta;
    //矩形数量
    private int itemNum;
    //第一个矩形索引
    private int firstItem;
    //当前矩形索引
    private int currentItem;
    //随机数对象
    private Random random;
    // 外矩形的长宽
    double W, H;
    // 是否允许旋转
    private boolean isRotateEnable;

    Solution localSolution;
    //构造函数
    public Ant(boolean isRotateEnable, double W, double H, Item[] items, Long seed) {
        this.itemNum = items.length;
        this.items = items;
        this.H = H;
        this.W = W;
        this.isRotateEnable = isRotateEnable;
        this.random = seed == null ? new Random() : new Random(seed);
    }
    //初始化
    public void init(double [][] different,double a,double b) {
        alpha = a;
        beta = b;
        this.different = different;
        //初始化允许搜索的矩形索引
        allowedItems = new ArrayList<>();
        //初始化禁忌表
        sequence = new ArrayList<>();
        //初始化信息素变化矩阵
        delta = new double[itemNum][itemNum];
        //设置起始矩形
        firstItem = random.nextInt(itemNum);

        for (int i = 0; i < itemNum; i++) {
            if (i != firstItem){
                allowedItems.add(i);
            }
        }
        //将第一个放置的矩形添加到禁忌表
        sequence.add(firstItem);
        currentItem = firstItem;//设置当前矩形为第一个矩形
    }
    //选择下一个矩形
    public void selectNextItem(double [][] pheromone) {
        double[] p = new double[itemNum];
        double sum = 0d;
        //采用不同度代替距离
        //计算分母部分
        for (Integer i : allowedItems) {
            sum += Math.pow(pheromone[currentItem][i], alpha)*Math.pow(1/different[currentItem][i], beta);
        }
        //计算概率
        for (int i : allowedItems) {
            p[i] = (Math.pow(pheromone[currentItem][i], alpha)*Math.pow(1/different[currentItem][i], beta))/sum;
        }
        //轮盘赌选择下一个矩形
        double selectP = random.nextDouble();
        int selectItem = -1;//记录选中的矩形索引
        double sumP = 0d;
        for (int i = 0; i < itemNum; i++) {
            sumP+=p[i];
            if (selectP <= sumP){
                selectItem = i;
                break;
            }
        }
        //在allowedItems中删除已选择的矩形
        for (Integer i : allowedItems) {
            if (i == selectItem){
                allowedItems.remove(i);
                break;
            }
        }
        //将已选择的矩形添加到禁忌表
        sequence.add(selectItem);
        currentItem = selectItem;
    }
}
