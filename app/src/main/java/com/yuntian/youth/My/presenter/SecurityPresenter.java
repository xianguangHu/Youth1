package com.yuntian.youth.My.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.My.view.callback.SecurityView;
import com.yuntian.youth.Utils.StringUtils;
import com.yuntian.youth.register.api.RegisterApi;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.RxSubscribe;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/24.
 */

public class SecurityPresenter extends MvpBasePresenter<SecurityView>{
    private Context mContext;
    public SecurityPresenter(Context context){
        mContext=context;
    }

    //发送验证短信
    public void sendSms(){
        String phone= User.getCurrentUser().getMobilePhoneNumber();
        if (StringUtils.isPhone(phone)){
            RegisterApi.SendSMS(phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscribe<Integer>(mContext) {
                        @Override
                        protected void _onNext(Integer integer) {
                            getView().Success();
                        }

                        @Override
                        protected void _onError(String message) {
                            getView().Erro(message);
                        }
                    });
            return;
        }
            getView().Erro("手机号不合法!");
    }
}
