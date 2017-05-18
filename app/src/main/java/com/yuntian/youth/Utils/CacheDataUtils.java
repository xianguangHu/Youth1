package com.yuntian.youth.Utils;

import android.content.Context;

import com.yuntian.youth.dynamic.model.Dynamic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    public static void saveRecentDynamic(String uid, List<Dynamic> list, Context context){
        int type=recent_dynamic_list;
        String fileName=getFileNameById(type,uid);
        File file=new File(context.getCacheDir(),fileName);
        try {
        if (!file.exists()) file.createNewFile();
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(list);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
