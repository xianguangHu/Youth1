package com.yuntian.youth.register.service;

import android.util.Log;

import com.yuntian.youth.App;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.BmobCallback1;

/**
 * Created by huxianguang on 2017/4/17.
 */

public class CheckService {
    public static void SMSRegister(String phone, String SMSCode, final BmobCallback1<BmobException> callback) {
        Log.v("==============", phone + "====" + SMSCode);
//        BmobUser.signOrLoginByMobilePhone(phone, SMSCode, new LogInListener<User>() {
//            @Override
//            public void done(final User user, BmobException e) {
//                if (e==null) {
////                    Observable<User> observable = Observable.create(new Observable.OnSubscribe<User>() {
////                        @Override
////                        public void call(Subscriber<? super User> subscriber) {
////                            subscriber.onNext(user);
////                            subscriber.onCompleted();
////                        }
////                    });
//                    callback.done(user);
//                }else {
//                    Log.v("一键注册",e+"");
//                }
//            }
//        });
        BmobSMS.verifySmsCode(App.getContext(), phone, SMSCode, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                callback.done(e);
            }
        });
    }
}
