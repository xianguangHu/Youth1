package com.yuntian.youth.My.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.My.service.MyDataService;
import com.yuntian.youth.My.view.callback.MyDataView;
import com.yuntian.youth.Utils.StringUtils;
import com.yuntian.youth.Utils.TimeUtil;
import com.yuntian.youth.widget.RxSubscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/23.
 */

public class MyDataPresenter extends MvpBasePresenter<MyDataView>{
    private Context mContext;
    public MyDataPresenter(Context context){
        mContext=context;
    }

    public void updateUserData(final String username, final String gender, final String birthday){

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                if (StringUtils.isUserDataUpdate(username,gender,birthday)){
                    //说明用户已经更改过信息 可以更新
                    subscriber.onNext(1);
                    subscriber.onCompleted();
                }else {
                    subscriber.onError(new Throwable("请您更改后再保存!"));
                }
            }
        })
        .flatMap(new Func1<Object, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Object o) {
                //根据生日计算出年龄
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date=format.parse(birthday);
                    int age= TimeUtil.getAge(date);
                    return Observable.just(age);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return Observable.just(0);
                }
            }
        })
                .flatMap(new Func1<Integer, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(Integer age) {

                        return MyDataService.updateUser(username,gender,birthday,age);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<Void>(mContext) {
                    @Override
                    protected void _onNext(Void aVoid) {
                        getView().UpdateSuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        getView().UpdateErro(message);
                    }
                });
    }
}
