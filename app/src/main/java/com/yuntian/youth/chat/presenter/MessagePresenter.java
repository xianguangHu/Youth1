package com.yuntian.youth.chat.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yuntian.youth.chat.model.ConversationItem;
import com.yuntian.youth.chat.service.MessageService;
import com.yuntian.youth.chat.view.callback.MessageView;
import com.yuntian.youth.register.model.bean.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/15.
 */

public class MessagePresenter extends MvpBasePresenter<MessageView> {
    private List<EMConversation> mEMConversationList;

    public MessagePresenter() {
        mEMConversationList = new ArrayList<>();
        loadAllConversations();
    }

    /**
     * 获取所有会话消息
     */
    public void loadAllConversations() {
        Observable.create(new Observable.OnSubscribe<List<EMConversation>>() {
            @Override
            public void call(Subscriber<? super List<EMConversation>> subscriber) {
                Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
                //先清除所有会话
                mEMConversationList.clear();
                mEMConversationList.addAll(conversationMap.values());
                //根据时间比较进行排序
                Collections.sort(mEMConversationList, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                    }
                });
                subscriber.onNext(mEMConversationList);
            }
        })
                .flatMap(new Func1<List<EMConversation>, Observable<EMConversation>>() {
            @Override
            public Observable<EMConversation> call(List<EMConversation> emConversations) {
                return Observable.from(emConversations);
            }
        })
                .flatMap(new Func1<EMConversation, Observable<ConversationItem>>() {
                    @Override
                    public Observable<ConversationItem> call(EMConversation emConversation) {
                        return Observable.zip(Observable.just(emConversation), MessageService.queryUser(emConversation), new Func2<EMConversation, User, ConversationItem>() {
                            @Override
                            public ConversationItem call(EMConversation emConversation, User user) {
                                return new ConversationItem(user,emConversation);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ConversationItem>() {
                    @Override
                    public void call(ConversationItem conversationItems) {
                        getView().update2Conversations(conversationItems);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.v("erro======", throwable.getMessage());
                    }
                });

    }
}
