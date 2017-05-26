package com.yuntian.youth.My.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/5/24.
 */

public interface SecurityView extends MvpView{

    void Success();
    void Erro(String erro);
}
