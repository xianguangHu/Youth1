package com.yuntian.youth.register.api;

import android.util.Log;

import com.yuntian.youth.register.model.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/15.
 */

public class RegisterApi {

    public static Observable<List<User>> getRegister(String phone){
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber",phone);
        Observable<List<User>> observable=query.findObjectsObservable(User.class);
        return observable;
    }

    public static Observable<Integer> SendSMS(String phone){
        Log.v("发送",phone);
        Observable<Integer> observable= BmobSMS.requestSMSCodeObservable(phone, "youth");
        return observable;
    }

}
