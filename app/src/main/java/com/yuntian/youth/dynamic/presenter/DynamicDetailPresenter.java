package com.yuntian.youth.dynamic.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.dynamic.model.Comment;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.service.DynamicDetailService;
import com.yuntian.youth.dynamic.view.callback.DynamicDetailView;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.RxSubscribe;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/10.
 */

public class DynamicDetailPresenter extends MvpBasePresenter<DynamicDetailView>{
    /**
     * 发送评论
     */
    public void sendComment(String content, Dynamic dynamic, User reolyAuthor, Context mContext){
        DynamicDetailService.sendComment(content,reolyAuthor,dynamic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<String>(mContext) {
                    @Override
                    protected void _onNext(String s) {
                        Log.v("=====","success");
                        getView().commentSuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        Log.v("=====",message);

                    }
                });

    }

    /**
     * 获取评论
     * @param dynamic
     */
    public void getData(Dynamic dynamic,Context context){
        Log.v("====","开始加载");
        DynamicDetailService.getComment(dynamic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<List<Comment>>(context) {
                    @Override
                    protected void _onNext(List<Comment> comments) {
                        Log.v("====",comments.size()+"");

                        getView().UpdateData2Comment(comments);
                    }

                    @Override
                    protected void _onError(String message) {
                        Log.v("==erro",message);
                    }
                });
    }
}
