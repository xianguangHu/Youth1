package com.yuntian.youth.chat.model;

import com.yuntian.youth.register.model.bean.User;

/**
 * Created by huxianguang on 2017/5/15.
 * 聊天的实体类
 */

public class ChatItem {
    private int type;

    private String messageContent;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
