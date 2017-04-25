package com.yuntian.youth.register.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.register.service.CheckService;
import com.yuntian.youth.register.view.callback.CheckView;

import cn.bmob.sms.exception.BmobException;
import cn.bmob.v3.listener.BmobCallback1;

/**
 * Created by huxianguang on 2017/4/17.
 */

public class CheckPresenter extends MvpBasePresenter<CheckView> {
    /**
     *   注册
     * @param phone 手机号
     * @param SMSCode 验证码
     */
    public void Register(String phone,String SMSCode){
        CheckService.SMSRegister(phone, SMSCode, new BmobCallback1<BmobException>() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    //验证成功
                    Log.v("验证成功","==========");
                    if (getView()!=null){
                        getView().Success();
                    }
                }else {
                    Log.v("验证失败",e+"");
                    if (getView()!=null){
                        getView().CheckErro("验证码错误,请重新输入!");
                    }
                }
            }
        });
    }
}
