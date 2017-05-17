package com.yuntian.youth.chat.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.yuntian.youth.chat.model.ConversationItem;

/**
 * Created by huxianguang on 2017/5/15.
 */

public interface MessageView extends MvpView {
    void update2Conversations(ConversationItem conversationItem);
}
