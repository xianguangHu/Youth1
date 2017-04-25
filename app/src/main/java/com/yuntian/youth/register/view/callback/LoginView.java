package com.yuntian.youth.register.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/4/19.
 */

public interface LoginView extends MvpView{
    void LoginSuccess();
    void LoginErro(String e);
}
