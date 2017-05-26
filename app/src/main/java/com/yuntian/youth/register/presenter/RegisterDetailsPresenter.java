package com.yuntian.youth.register.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yuntian.youth.Utils.Encrypt;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.service.RegisterService;
import com.yuntian.youth.register.view.callback.RegisterDetailsView;
import com.yuntian.youth.widget.RxSubscribe;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/4/18.
 */

public class RegisterDetailsPresenter extends MvpBasePresenter<RegisterDetailsView> {


    private Context mContext;
    public RegisterDetailsPresenter(Context context){
        mContext=context;
    }
    /**
     * 注册或者重置
     * @param phone
     * @param password
     * @param type
     * @param code 验证码只有在重置密码是有用
     */
    public void Register(String phone,String password,int type,String code){
        //加密密码
         String Md5ShaPassword = Encrypt.md5_sha(password);
        if (Constant.CODE_REGISTER==type) {
            //注册
            RegisterService.Register(phone, Md5ShaPassword)
                    .flatMap(new Func1<User, Observable<Integer>>() {
                        @Override
                        public Observable<Integer> call(final User user) {
                            //创建环信的用户

                            Log.v("注册====环信", "huanx");
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        EMClient.getInstance().createAccount(user.getObjectId(), user.getObjectId());
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                        Observable.error(new Throwable("聊天通道注册失败"));
                                    }
                                }
                            }.start();

                            return Observable.just(1);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer user) {
                            //注册成功 到首页
                            Log.v("注册成功", "======");
                            getView().RegisterSuccess();
                        }
                    }, new Action1<Throwable>() {

                        @Override
                        public void call(Throwable throwable) {
                            //注册失败
                            Log.v("注册失败", "======" + throwable);
                            getView().RegisterErro(throwable.getMessage());

                        }
                    });
        }else if (Constant.CODE_PASSWORD==type){
            //重置密码
            Log.v("重置密码的验证码=======",code);
            if (code!=null) {
                RegisterService.resetPassword(code,Md5ShaPassword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscribe<Void>(mContext) {
                            @Override
                            protected void _onNext(Void aVoid) {
                                getView().RegisterSuccess();
                            }

                            @Override
                            protected void _onError(String message) {
                                getView().RegisterErro(message);
                            }
                        });
            }else {
                getView().RegisterErro("重置密码失败-验证码为空!");
            }

        }
    }
}
