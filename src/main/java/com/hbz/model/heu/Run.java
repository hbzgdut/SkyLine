package com.hbz.model.heu;

import com.hbz.entitys.Instance;
import com.hbz.entitys.Item;
import com.hbz.util.ReadDataUtil;

public class Run {
    public static void main(String[] args) throws  Exception{
        String path = "E:\\MyCode\\2D_rectangular_layout\\SkyLine\\src\\main\\java\\com\\hbz\\data\\data.txt";
        ReadDataUtil readDataUtil = new ReadDataUtil();
        Instance instance = readDataUtil.getInstance(path);
        System.out.println("放置框宽:" + instance.getWidth()+" 放置框长:"+instance.getHeight()+" 是否允许旋转:"+instance.isRotateEnable());
        //instance.getItemsList().forEach(item -> System.out.println("宽"+item.getW()+"高"+item.getH()+"名字"+item.getName()));
        Item[] items = instance.getItemsList().toArray(new Item[instance.getItemsList().size()]);
        new TabuSearch(200, 50, 10, instance, null).solve();
    }
}
