package com.yuntian.youth.chat.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yuntian.youth.chat.view.callback.MessageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/15.
 */

public class MessagePresenter extends MvpBasePresenter<MessageView>{
    private List<EMConversation> mEMConversationList;

    public MessagePresenter(){
        mEMConversationList=new ArrayList<>();
        loadAllConversations();
    }
    /**
     * 获取所有会话消息
     */
    public void loadAllConversations(){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Map<String,EMConversation> conversationMap= EMClient.getInstance().chatManager().getAllConversations();
                //先清除所有会话
                mEMConversationList.clear();
                mEMConversationList.addAll(conversationMap.values());
                //根据时间比较进行排序
                Collections.sort(mEMConversationList, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
                    }
                });
                subscriber.onNext(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getView().update2Conversations(mEMConversationList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.v("erro======",throwable.getMessage());
                    }
                });
    }
}
