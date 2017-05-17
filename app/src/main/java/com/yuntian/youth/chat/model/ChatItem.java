package com.yuntian.youth.chat.model;

import com.hyphenate.chat.EMMessage;
import com.yuntian.youth.register.model.bean.User;

/**
 * Created by huxianguang on 2017/5/15.
 * 聊天的实体类
 */

public class ChatItem {
    private int type;

    private User user;

    private EMMessage mEMMessage;

    private String context;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public EMMessage getEMMessage() {
        return mEMMessage;
    }

    public void setEMMessage(EMMessage EMMessage) {
        mEMMessage = EMMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
