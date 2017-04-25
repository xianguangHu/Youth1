package com.yuntian.youth.register.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/4/14.
 */

public interface RegisterView extends MvpView {
    void startLogin(String phone);
    void StartRegister(String phone);
    void RegisterErro(String e);
}
