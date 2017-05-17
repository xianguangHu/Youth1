package com.yuntian.youth.chat.listener;

import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by huxianguang on 2017/5/16.
 */

public class EMMessageListenerAdapter implements EMMessageListener {
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        Log.v("onMessageReceived","=============");
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        Log.v("onCmdMessageReceived","=============");

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        Log.v("onMessageRead","=============");

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        Log.v("onMessageDelivered","=============");

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        Log.v("onMessageChanged","=============");

    }
}
