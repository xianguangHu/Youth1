package com.yuntian.youth.dynamic.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/4/25.
 */

public interface ReleaseView extends MvpView {
    void sendSuccess();
    void sendErro(String e);
}
