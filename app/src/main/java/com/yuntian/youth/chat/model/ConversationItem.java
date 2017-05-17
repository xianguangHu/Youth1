package com.yuntian.youth.chat.model;

import com.hyphenate.chat.EMConversation;
import com.yuntian.youth.register.model.bean.User;

/**
 * Created by huxianguang on 2017/5/16.
 * 会话的item
 */

public class ConversationItem {
    public ConversationItem(User user,EMConversation eMConversation){
        this.user=user;
        this.eMConversation=eMConversation;
    }
    private User user;
    private EMConversation eMConversation;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EMConversation geteMConversation() {
        return eMConversation;
    }

    public void seteMConversation(EMConversation eMConversation) {
        this.eMConversation = eMConversation;
    }
}
