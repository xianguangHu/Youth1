package com.yuntian.youth.dynamic.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by huxianguang on 2017/5/17.
 */

public interface LoadImageView extends MvpView{
    void onDownLoadSuccess();
    void onDownLoadErro();
}
