package com.yuntian.youth.My.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/4/23.
 */

public interface MyView extends MvpView {
    void UpdateHeadSuccess(String uri);
    void Erro(String e);
}
