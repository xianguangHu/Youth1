package com.yuntian.youth.chat.service;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatSevice {
    public static void sendMessage(String content,String toChatUsername){
        //创建一条文本信息
        EMMessage message=EMMessage.createTxtSendMessage(content,toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }
}
