package com.yuntian.youth.Utils;

import android.content.Context;
import android.os.Environment;

/**
 * Created by huxianguang on 2017/4/22.
 */

public class SDFilesUtils {
    /**
     * 获取app缓存路径
     * @param context
     * @return
     */
    public static String getCachePath( Context context ){
        String cachePath ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath() ;
        }else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath() ;
        }
        return cachePath ;
    }
}
