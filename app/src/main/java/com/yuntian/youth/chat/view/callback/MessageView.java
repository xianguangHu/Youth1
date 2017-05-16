package com.yuntian.youth.chat.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by huxianguang on 2017/5/15.
 */

public interface MessageView extends MvpView {
    void update2Conversations(List<EMConversation> emConversations);
}
