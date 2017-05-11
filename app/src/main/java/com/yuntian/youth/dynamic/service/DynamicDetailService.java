package com.yuntian.youth.dynamic.service;

import com.yuntian.youth.dynamic.model.Comment;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.register.model.bean.User;

import rx.Observable;

/**
 * Created by huxianguang on 2017/5/10.
 */

public class DynamicDetailService {
    public static Observable<String> sendComment(String content, User reolyAuthor,Dynamic dynamic){
        boolean isReply=false;
        if (reolyAuthor!=null) isReply=true;
        Comment comment=new Comment();
        comment.setDynamic(dynamic);
        comment.setContent(content);
        comment.setAuthor(User.getCurrentUser());
        comment.setReply(isReply);
        comment.setReplyAuthor(reolyAuthor);
        Observable<String> observable=comment.saveObservable();
        return observable;
    }
}
