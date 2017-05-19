package com.yuntian.youth.Utils;

import android.content.Context;

import com.yuntian.youth.dynamic.model.Dynamic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxianguang on 2017/5/17.
 * 保存缓存文件
 */

public class CacheDataUtils {
    public static final int recent_dynamic_list =8;
    /**
     * 获取文件名
     * @param type 数据类型
     * @param uid 用户的id
     * @return
     */
    public static String getFileNameById(int type,String uid){
        String fileName="cachedata_"+uid+"_"+type+".dat";
        return fileName;
    }

    /**
     * 保存缓存文件
     * @param uid
     * @param list
     * @param context
     */
    public static void saveRecentDynamic(String uid, List<Dynamic> list, Context context){
        int type=recent_dynamic_list;
        String fileName=getFileNameById(type,uid);
        File file=new File(context.getCacheDir(),fileName);
        try {
        if (!file.exists()) file.createNewFile();
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(list);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存文件
     * @param uid
     * @param context
     * @return
     */
    public static List<Dynamic> getRecentDyanmic(String uid,Context context){
        int type=recent_dynamic_list;
        List<Dynamic> list=new ArrayList<>();
        String fileName=getFileNameById(type,uid);
        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(context.getCacheDir()+"/"+fileName));
            ArrayList<Dynamic> list_dynamic= (ArrayList<Dynamic>) ois.readObject();
            for (Dynamic dynamic:list_dynamic){
                Dynamic bean=dynamic;
                if (bean!=null){
                    list.add(bean);
                }
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }
}
