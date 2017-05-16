package com.yuntian.youth.chat.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.yuntian.youth.chat.model.ChatItem;
import com.yuntian.youth.chat.service.ChatSevice;
import com.yuntian.youth.chat.view.callback.ChatView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatPresenter extends MvpBasePresenter<ChatView>{

    public ChatPresenter(){
        //监听消息
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }

    //发送信息
    public void sendMessage(final String message, final User user){

                ChatSevice.sendMessage(message,user.getObjectId());

        getView().MessageSuccess(message);
    }

    EMMessageListener mEMMessageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            //接受消息
            Log.v("========接受消息",messages.get(0).toString());
            Observable.create(new Observable.OnSubscribe<List<ChatItem>>() {
                @Override
                public void call(Subscriber<? super List<ChatItem>> subscriber) {
                    List<ChatItem> chatItemList=new ArrayList<>();
                    for (EMMessage emMessage:messages){
                        ChatItem chatItem =new ChatItem();
                        String context=((EMTextMessageBody)emMessage.getBody()).getMessage();
                        chatItem.setMessageContent(context);
                        chatItem.setType(Constant.CHAT_MESSAGE_TYPE_FRIENDS);
                        chatItem.setUser(User.getCurrentUser());
                        chatItemList.add(chatItem);
                    }
                    subscriber.onNext(chatItemList);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<ChatItem>>() {
                        @Override
                        public void call(List<ChatItem> chatItems) {
                            getView().MessageReceived(chatItems);

                        }
                    });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //接受透传消息
            Log.v("========接受透传消息",messages.get(0).toString());

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.v("========收到已读回执",messages.get(0).toString());

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            //收到一送达回执
            Log.v("========收到一送达回执",messages.get(0).toString());

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态改变
            Log.v("========消息状态改变","====");

        }
    };
}
