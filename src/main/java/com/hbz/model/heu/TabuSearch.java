package com.hbz.model.heu;

import com.hbz.entitys.Instance;
import com.hbz.entitys.Item;
import com.hbz.entitys.Solution;
import com.hbz.model.skyline.SkyLinePacking;

import java.util.Random;

public class TabuSearch {
    private int MAX_GEN;// 最大迭代次数
    private int tabuSize;// 禁忌表长度
    private int N;//局部搜索次数
    //禁忌表 - 存储交换操作(r1, r2)而非完整序列
    private int[][] tabuList;
    public int bestT = -1;
    public Random random;
    public int len = 0;//当前禁忌表长度

    private double H;
    private double W;
    Item[] items;
    private boolean isRotateEnable;
    private Solution bestSolutionGlobal; // 全局最优解，用于特赦准则

    public TabuSearch(int MAX_GEN, int N, int tabuSize, Instance instance, Long seed){
        this.MAX_GEN = MAX_GEN;
        this.N = N;
        this.tabuSize = tabuSize;
        this.H = instance.getHeight();
        this.W = instance.getWidth();
        this.items = Item.copy(instance.getItemsList().toArray(new Item[instance.getItemsList().size()]));
        this.isRotateEnable = instance.isRotateEnable();
        this.random = seed==null?new Random():new Random(seed);
    }

    public Solution solve(){
        //初始化禁忌表
        tabuList = new int[tabuSize][items.length];
        //按面积排序
        java.util.Arrays.sort(items, (a, b) -> Double.compare(b.getW() * b.getH(), a.getW() * a.getH()));
        // 获取初始解 [ 0, 1, 2, 3,... ,n ]
        int[] sequence = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            sequence[i] = i;
        }
        Solution bestSolution = evaluate(sequence);
        int[] bestSequence = sequence.clone();
        System.out.println("初始解:"+bestSolution.getRate());
        for (int gen = 0; gen < MAX_GEN; gen++) {
            Solution localSolution = null;
            int[] localSequence = null;
            boolean isTabu = false;
            
            // 在N个邻域中寻找最佳非禁忌解
            for (int i = 0; i < N; i++) {
                int[] tempSequence = generateANewSequence(sequence);
                Solution tempSolution = evaluate(tempSequence);
                
                // 检查是否禁忌
                boolean currentIsTabu = isTabuList(tempSequence);
                
                // 特赦准则：如果优于全局最优，即使在禁忌表中也接受
                if (currentIsTabu) {
                    if (bestSolutionGlobal != null && tempSolution.getRate() > bestSolutionGlobal.getRate()) {
                        // 满足特赦条件
                        localSolution = tempSolution;
                        localSequence = tempSequence;
                        isTabu = true;
                        break; // 找到特赦解，直接跳出
                    }
                } else {
                    // 非禁忌解，正常比较
                    if(localSolution == null || tempSolution.getRate() > localSolution.getRate()){
                        localSolution = tempSolution;
                        localSequence = tempSequence;
                        isTabu = false;
                    }
                }
            }
            
            // 如果所有邻域都被禁忌且不满足特赦条件，生成随机解
            if (localSequence == null) {
                localSequence = generateANewSequence(sequence);
                localSolution = evaluate(localSequence);
                isTabu = false;
            }
            
            // 更新全局最优解
            if (bestSolutionGlobal == null || localSolution.getRate() > bestSolutionGlobal.getRate()) {
                bestSolutionGlobal = localSolution;
            }
            
            // 更新当前代的最优解
            if (localSolution.getRate() > bestSolution.getRate()){
                bestSolution = localSolution;
                bestSequence = localSequence;
                bestT = gen;
                System.out.println("第"+(gen+1)+"代:"+bestSolution.getRate());
            }
            
            sequence = localSequence.clone();
            // 只有非特赦的解才加入禁忌表
            if (!isTabu) {
                enterTabuList(localSequence);
            }
        }
        return bestSolution;
    }

    private void enterTabuList(int[] sequence) {
        if(len<tabuSize){
            tabuList[len] = sequence.clone();
            len++;
        }else{
            for (int i = 0; i < tabuSize-1; i++) {
                tabuList[i] = tabuList[i+1].clone();
            }
            tabuList[tabuList.length-1] = sequence.clone();
        }
    }

    private boolean isTabuList(int[] tempSequence) {
        for (int[] ints : tabuList) {
            if (java.util.Arrays.equals(tempSequence, ints)) {
                return true;
            }
        }
        return false;
    }

    private int[] generateANewSequence(int[] sequence) {
        int[] tempSequence = sequence.clone();
        int r1 = random.nextInt(sequence.length);
        int r2 = random.nextInt(sequence.length);
        while (r1 == r2) {
            r2 = random.nextInt(sequence.length);
        }
        int temp = tempSequence[r1];
        tempSequence[r1] = tempSequence[r2];
        tempSequence[r2] = temp;
        return tempSequence;
    }

    private Solution evaluate(int[] sequence) {
        Item[] items = new Item[this.items.length];
        for (int i = 0; i <sequence.length ; i++) {
            items[i] = this.items[sequence[i]];
        }
        return new SkyLinePacking(isRotateEnable, W, H, items).packing();
    }
}
