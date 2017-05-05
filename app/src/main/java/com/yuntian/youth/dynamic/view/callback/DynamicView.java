package com.yuntian.youth.dynamic.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.yuntian.youth.dynamic.model.DynamicDateil;

import java.util.List;

/**
 * Created by huxianguang on 2017/4/27.
 */

public interface DynamicView extends MvpView{
    void update2loadData(List<DynamicDateil> datas);
    void updateLike(int position);
}
