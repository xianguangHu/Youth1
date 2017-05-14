package com.yuntian.youth.register.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yuntian.youth.Utils.Encrypt;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.service.RegisterService;
import com.yuntian.youth.register.view.callback.LoginView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/4/19.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView>{

    public void Login(String phone,String password){
        String Md5ShaPassword= Encrypt.md5_sha(password);
        RegisterService.Login(phone,Md5ShaPassword)
                .flatMap(new Func1<User, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(final User user) {

                        return Observable.create(new Observable.OnSubscribe<Integer>() {
                            @Override
                            public void call(final Subscriber<? super Integer> subscriber) {
                                EMClient.getInstance().login(user.getObjectId(), user.getObjectId(), new EMCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        EMClient.getInstance().chatManager().loadAllConversations();
                                        Log.v("聊天服务器登陆成功","========");
                                        subscriber.onNext(1);
                                    }

                                    @Override
                                    public void onError(int code, String error) {
                                        subscriber.onError(new Throwable(code+"+"+error));
                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {

                                    }
                                });
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer user) {
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
