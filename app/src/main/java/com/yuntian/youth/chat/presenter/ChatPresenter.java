package com.yuntian.youth.chat.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yuntian.youth.chat.service.ChatSevice;
import com.yuntian.youth.chat.view.callback.ChatView;
import com.yuntian.youth.register.model.bean.User;

import java.util.List;

/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatPresenter extends MvpBasePresenter<ChatView>{

    public ChatPresenter(){
        //监听消息
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }

    //发送信息
    public void sendMessage(String message,User user){
        ChatSevice.sendMessage(message,user.getObjectId());
        getView().MessageSuccess(message);
    }

    EMMessageListener mEMMessageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //接受消息
            Log.v("========接受消息",messages.get(0).toString());
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
