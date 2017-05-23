package com.yuntian.youth.listener;

import android.content.DialogInterface;

/**
 * Created by huxianguang on 2017/5/23.
 */

public interface YouthListeren {
    void dialogCall(DialogInterface dialog, int which);

    void dialogCall(String content,int type);

}
