package com.yuntian.youth.My.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/5/23.
 */

public interface MyDataView extends MvpView{
    void UpdateSuccess();
    void UpdateErro(String erro);
}
