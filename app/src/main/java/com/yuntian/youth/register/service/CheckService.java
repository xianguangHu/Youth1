package com.yuntian.youth.register.service;

import android.util.Log;

import com.yuntian.youth.App;
import com.yuntian.youth.register.model.bean.User;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.BmobCallback1;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/17.
 */

public class CheckService {
    public static void SMSRegister(String phone, String SMSCode, final BmobCallback1<BmobException> callback) {
        Log.v("==============", phone + "====" + SMSCode);

        BmobSMS.verifySmsCode(App.getContext(), phone, SMSCode, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                callback.done(e);
            }
        });
    }

    public static Observable<Void> updatePhonenumber(String phone){
        BmobUser newuser=new BmobUser();
        newuser.setMobilePhoneNumber(phone);
        newuser.setMobilePhoneNumberVerified(true);
        Observable<Void> observable=newuser.updateObservable(User.getCurrentUser().getObjectId());
        return observable;
    }
}
