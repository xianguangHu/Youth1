package com.yuntian.youth.chat.view.callback;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.yuntian.youth.chat.model.ChatItem;

import java.util.List;

/**
 * Created by huxianguang on 2017/5/14.
 */

public interface ChatView extends MvpView{
    void MessageSuccess(String message);

    void MessageReceived(List<ChatItem> chatItems);
    void LoadMoreSuccess(List<ChatItem> chatItems);
}
