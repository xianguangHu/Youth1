package com.yuntian.youth;

import android.app.Application;
import android.content.Context;

import com.yuntian.youth.global.Constant;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;

/**
 * Created by huxianguang on 2017/4/15.
 */

public class App extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        Bmob.initialize(this, Constant.APPLICATION_ID);
        BmobSMS.initialize(this,Constant.APPLICATION_ID);
    }
    public static Context getContext(){
        return mContext;
    }
}
