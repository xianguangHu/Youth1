package com.yuntian.youth.register.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/4/17.
 */

public interface CheckView extends MvpView {
    void CheckErro(String e);
    void Success();
}
