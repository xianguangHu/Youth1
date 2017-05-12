package com.yuntian.youth.dynamic.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.yuntian.youth.dynamic.model.Comment;

import java.util.List;

/**
 * Created by huxianguang on 2017/5/10.
 */

public interface DynamicDetailView extends MvpView{
    void commentSuccess();
    void UpdateData2Comment(List<Comment> datas);
}
