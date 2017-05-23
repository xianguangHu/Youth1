package com.yuntian.youth.My.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.My.view.callback.ReplaceNumberView;
import com.yuntian.youth.Utils.StringUtils;
import com.yuntian.youth.register.api.RegisterApi;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.RxSubscribe;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/24.
 * 更换手机号业务层
 */

public class ReplaceNumberPresenter extends MvpBasePresenter<ReplaceNumberView>{
    private Context mContext;
    public ReplaceNumberPresenter(Context context){
        mContext=context;
    }

    public void sendSMS(final String phone){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (StringUtils.isPhone(phone)){
                    //判断符合手机号规则
                    if (!User.getCurrentUser().getMobilePhoneNumber().equals(phone)){
                        //说明手机号与原来手机号不一样  可以更换
                        subscriber.onNext(1);
                        subscriber.onCompleted();
                    }else {
                        //和原来手机号一样
                        subscriber.onError(new Throwable("请勿输入相同手机号"));
                    }
                }else {
                    //不符合
                    subscriber.onError(new Throwable("手机号不合法!"));
                }
            }
        })
                .flatMap(new Func1<Integer, Observable<List<User>>>() {
                    @Override
                    public Observable<List<User>> call(Integer integer) {
                        return RegisterApi.getRegister(phone);
                    }
                })
                .flatMap(new Func1<List<User>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(List<User> users) {
                        if (users.size()>0){
                            return Observable.error(new Throwable("此号码已注册!"));
                        }else {
                            //说明号码没有人注册过可以更换号码
                            return RegisterApi.SendSMS(phone);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<Integer>(mContext) {
                    @Override
                    protected void _onNext(Integer integer) {
                        getView().Success(phone);
                    }

                    @Override
                    protected void _onError(String message) {
                        getView().Erro(message);
                    }
                });
    }
}
