package com.yuntian.youth.register.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.service.CheckService;
import com.yuntian.youth.register.view.callback.CheckView;
import com.yuntian.youth.widget.RxSubscribe;

import cn.bmob.sms.exception.BmobException;
import cn.bmob.v3.listener.BmobCallback1;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/4/17.
 */

public class CheckPresenter extends MvpBasePresenter<CheckView> {
    private Context mContext;
    public CheckPresenter(Context context){
        mContext=context;
    }
    /**
     *   注册
     * @param phone 手机号
     * @param SMSCode 验证码
     */
    public void Register(final String phone, final String SMSCode, final int type){

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                CheckService.SMSRegister(phone, SMSCode, new BmobCallback1<BmobException>() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            //验证成功
                            Log.v("验证成功","==========");
                            if (getView()!=null){
                                subscriber.onNext(type);
                                subscriber.onCompleted();
                            }
                        }else {
                            Log.v("验证失败",e+"");
                            subscriber.onError(new Throwable("验证码错误,请重新输入!"));
                        }
                    }
                });
            }
        }).flatMap(new Func1<Integer, Observable<Void>>() {
            @Override
            public Observable<Void> call(Integer integer) {
                if (type== Constant.CODE_REGISTER){//注册
                    Void viod=null;
                    return Observable.just(viod);
                }else if (type==Constant.CODE_REPLACE){//是更改手机号 需要更新用户表
                    return CheckService.updatePhonenumber(phone);
                }else if (type==Constant.CODE_PASSWORD){//修改用户密码
                    Void viod=null;
                    return Observable.just(viod);
                }
                return Observable.error(new Throwable("系统出错了!"));
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<Void>(mContext) {
                    @Override
                    protected void _onNext(Void aVoid) {
                        getView().Success();
                    }

                    @Override
                    protected void _onError(String message) {
                        getView().CheckErro(message);
                    }
                });


    }
}
