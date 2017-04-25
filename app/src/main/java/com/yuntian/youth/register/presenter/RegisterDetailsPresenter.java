package com.yuntian.youth.register.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.Utils.Encrypt;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.service.RegisterService;
import com.yuntian.youth.register.view.callback.RegisterDetailsView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/4/18.
 */

public class RegisterDetailsPresenter extends MvpBasePresenter<RegisterDetailsView> {
    public void Register(String phone,String password){
        String Md5ShaPassword= Encrypt.md5_sha(password);
        Log.v("加密后的密码",Md5ShaPassword);
        RegisterService.Register(phone,Md5ShaPassword)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //显示dialog
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        //注册成功 到首页
                        Log.v("注册成功","======");
                        if (getView()!=null){
                            getView().RegisterSuccess();
                        }
                    }
                },new Action1<Throwable>(){

                    @Override
                    public void call(Throwable throwable) {
                        //注册失败
                        Log.v("注册失败","======"+throwable);
                        if (getView()!=null){
                            getView().RegisterErro(throwable.getMessage());
                        }

                    }
                });
    }
}
