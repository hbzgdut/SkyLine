package com.hbz.util;

import com.hbz.entitys.Instance;
import com.hbz.entitys.Item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReadDataUtil {
    public Instance getInstance(String path) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String line;// 读取一行
        boolean isFirstLine = true;
        Instance instance = new Instance();
        List<Item> items = new ArrayList<>();// 存放矩形
        while ((line = bufferedReader.readLine()) != null) {
            String [] strs = line.split(" ");// 按空格分割
            if (isFirstLine) {
                instance.setWidth(Integer.parseInt(strs[0]));
                instance.setHeight(Integer.parseInt(strs[1]));
                instance.setRotateEnable(strs[2].equals("1"));
                isFirstLine = false;
            }else{
                String name = "rect_" + (items.size()+1);
                items.add(new Item(Double.parseDouble(strs[0]), Double.parseDouble(strs[1]),name));
            }
        }
        bufferedReader.close();
        instance.setItemsList(items);
        return instance;
    }
}
