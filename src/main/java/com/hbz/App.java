package com.hbz;

import com.hbz.entitys.Instance;
import com.hbz.entitys.Item;
import com.hbz.entitys.Solution;
import com.hbz.model.skyline.SkyLinePacking;
import com.hbz.util.ReadDataUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        String path = "E:\\MyCode\\2D_rectangular_layout\\SkyLine\\src\\main\\java\\com\\hbz\\data\\data.txt";
        ReadDataUtil readDataUtil = new ReadDataUtil();
        Instance instance = readDataUtil.getInstance(path);
        System.out.println("放置框宽:" + instance.getWidth()+" 放置框长:"+instance.getHeight()+" 是否允许旋转:"+instance.isRotateEnable());
        //instance.getItemsList().forEach(item -> System.out.println("宽"+item.getW()+"高"+item.getH()+"名字"+item.getName()));
        Item[] items = instance.getItemsList().toArray(new Item[instance.getItemsList().size()]);
        // 按面积从大到小排序
        java.util.Arrays.sort(items, (a, b) -> Double.compare(b.getW() * b.getH(), a.getW() * a.getH()));
//        System.out.println("排序后：");
//        for (Item item : items) {
//            System.out.println("宽"+item.getW()+"高"+item.getH()+"名字"+item.getName());
//        }
        SkyLinePacking skyLinePacking = new SkyLinePacking(instance.isRotateEnable(), Double.valueOf(instance.getWidth()), Double.valueOf(instance.getHeight()), items);
        Solution solution = skyLinePacking.packing();
        System.out.println("----------------------------------");
        solution.getPlaceItems().forEach(placeItem -> System.out.println("宽"+placeItem.getW()+"高"+placeItem.getH()+"名字"+placeItem.getName()));
        System.out.println("放置矩形数量为："+solution.getPlaceItems().size()+" /总矩形数量 " + items.length);
        System.out.println("放置率:"+solution.getRate()+" 放置面积:"+solution.getArea());

//        SkyLinePacking skyLinePacking = new SkyLinePacking(instance.isRotateEnable(), Double.valueOf(instance.getWidth()), Double.valueOf(instance.getHeight()), items);
//        Solution solution = skyLinePacking.packing();
//        System.out.println("放置率:"+solution.getRate()+" 放置面积:"+solution.getArea());
//        System.out.println("容器面积:" + (Double.valueOf(instance.getWidth()) * Double.valueOf(instance.getHeight())));
//        System.out.println("放置矩形数量:" + solution.getPlaceItems().size() + " / " + items.length);

    }
}
