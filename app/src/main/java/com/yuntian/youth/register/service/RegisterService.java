package com.yuntian.youth.register.service;

import android.util.Log;

import com.yuntian.youth.register.model.bean.User;

import cn.bmob.v3.BmobUser;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/15.
 */

public class RegisterService {
    /**
     * 注册
     * @param phone 手机号
     * @param password 加密后的密码
     * @return
     */
    public static Observable<User> Register(String phone,String password){
        BmobUser user=new BmobUser();
        user.setMobilePhoneNumber(phone);
        user.setMobilePhoneNumberVerified(true);
        user.setPassword(password);
        user.setUsername(phone);
        Observable<User> observable= user.signUpObservable(User.class);
        return observable;
    }


    public static Observable<User> Login(String phone,String password){
        Observable<User> override=BmobUser.loginByAccountObservable(User.class,phone,password);
        return override;
    }

    /**
     * 重置密码
     * @param code 手机验证码
     * @param password 加密后的密码
     * @return
     */
    public static Observable<Void> resetPassword(String code,String password){
        Log.v("==========",code);
        Observable<Void> observable=BmobUser.resetPasswordBySMSCodeObservable(code,password);
        return observable;
    }
}
