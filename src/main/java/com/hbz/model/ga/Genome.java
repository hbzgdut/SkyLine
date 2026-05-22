package com.hbz.model.ga;

import com.hbz.entitys.Item;
import com.hbz.entitys.Solution;
import com.hbz.model.skyline.SkyLinePacking;

public class Genome {
    //Genome对应每条染色体，评价这条染色体的利用率
    private Double W;//放置框宽
    private Double H;//放置框高
    private Item[] items;//矩形数组
    private boolean isRotateEnable;//是否允许旋转
    private int[] geneSequence;//基因序列,对应矩形数组的索引
    private double fitness;//适应度，就是利用率
    private Solution solution;// 序列对应的装载结果

    public Genome(Double w, Double h, Item[] items, boolean isRotateEnable, int[] geneSequence) {
        W = w;
        H = h;
        this.items = items;
        this.isRotateEnable = isRotateEnable;
        this.geneSequence = geneSequence;
    }

    public void updateFitnessAndSolution() {
        Item[] items1 = new Item[this.items.length];
        for (int i = 0; i < geneSequence.length; i++) {
            items1[i] = this.items[geneSequence[i]];
        }
        solution = new SkyLinePacking(isRotateEnable, W, H, items1).packing();
        fitness = solution.getRate();
    }

    public Double getW() {
        return W;
    }

    public void setW(Double w) {
        W = w;
    }

    public Double getH() {
        return H;
    }

    public void setH(Double h) {
        H = h;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public boolean isRotateEnable() {
        return isRotateEnable;
    }

    public void setRotateEnable(boolean rotateEnable) {
        isRotateEnable = rotateEnable;
    }

    public int[] getGeneSequence() {
        return geneSequence;
    }

    public void setGeneSequence(int[] geneSequence) {
        this.geneSequence = geneSequence;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }
}
