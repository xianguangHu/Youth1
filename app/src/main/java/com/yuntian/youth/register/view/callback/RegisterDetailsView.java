package com.yuntian.youth.register.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/4/18.
 */

public interface RegisterDetailsView extends MvpView{
    void RegisterSuccess();
    void RegisterErro(String e);
}
