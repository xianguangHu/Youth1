package com.yuntian.youth.chat.service;

import com.hyphenate.chat.EMConversation;
import com.yuntian.youth.register.model.bean.User;

import cn.bmob.v3.BmobQuery;
import rx.Observable;

/**
 * Created by huxianguang on 2017/5/16.
 */

public class MessageService {
    /**
     * 根据环信id查询user
     * @param emConversation
     * @return
     */
    public static Observable<User> queryUser(EMConversation emConversation){
        BmobQuery<User> query=new BmobQuery<>();
        Observable<User> observable=query.getObjectObservable(User.class,emConversation.conversationId());
        return observable;
    }
}
