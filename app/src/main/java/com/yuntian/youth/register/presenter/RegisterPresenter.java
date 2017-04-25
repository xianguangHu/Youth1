package com.yuntian.youth.register.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.Utils.StringUtils;
import com.yuntian.youth.register.api.RegisterApi;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.view.callback.RegisterView;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/4/14.
 */

public class RegisterPresenter extends MvpBasePresenter<RegisterView>{
    private RegisterView view;
    /**
     *  讲传入的手机号先判断是否已经注册
     *  如果已经注册 登陆界面
     *  如果没有注册 发送验证码注册
     * @param phone 手机号
     */
    public void go(String phone){
        view=getView();
        //现将phone验证
        if (StringUtils.isPhone(phone)) {
            LoginAndRegister(phone);
        }else {
            if (view!=null){
                view.RegisterErro("手机号不合法");
            }
        }
    }

    /**
     *  检查手机号是否已经注册过
     *      注册过跳到登陆界面
     *      没有就注册
     * @param phone
     */
    private void LoginAndRegister(final String phone){
        RegisterApi.getRegister(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<User>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(List<User> users) {
                        if (users.size() > 0) {
                            //不可以注册 调转到登陆界面
                            return Observable.just(1);
                        } else {
                            Observable<Integer> observable = RegisterApi.SendSMS(phone);
                            return observable;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (view != null) {
                            if (integer == 1) {
                                //登陆界面
                                view.startLogin(phone);
                            } else {
                                //验证码已经发送  注册界面
                                view.StartRegister(phone);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //验证码发送失败
                        Log.v("错误", "'======");
                    }
                });
    }

}
