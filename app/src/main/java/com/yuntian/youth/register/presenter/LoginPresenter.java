package com.yuntian.youth.register.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.Utils.Encrypt;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.service.RegisterService;
import com.yuntian.youth.register.view.callback.LoginView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/4/19.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView>{

    public void Login(String phone,String password){
        String Md5ShaPassword= Encrypt.md5_sha(password);
        RegisterService.Login(phone,Md5ShaPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        //登陆成功
                        Log.v("登陆成功","======");
                        if (getView()!=null){
                            getView().LoginSuccess();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.v("登陆失败","======"+throwable);
                        if (getView()!=null){
                            getView().LoginErro(throwable.getMessage());
                        }
                    }
                });
    }
}
