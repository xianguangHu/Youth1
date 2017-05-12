package com.yuntian.youth.dynamic.service;

import com.yuntian.youth.dynamic.model.Comment;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.register.model.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import rx.Observable;

/**
 * Created by huxianguang on 2017/5/10.
 */

public class DynamicDetailService {
    /**
     * 发送评论
     * @param content
     * @param reolyAuthor
     * @param dynamic
     * @return
     */
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

    /**
     * 获取照片动态的评论
     * @param dynamic
     * @return
     */
    public static Observable<List<Comment>> getComment(Dynamic dynamic){
        BmobQuery<Comment> query=new BmobQuery();
        query.addWhereEqualTo("dynamic",dynamic);
        query.include("author,replyAuthor");
        Observable<List<Comment>> observable=query.findObjectsObservable(Comment.class);
        return observable;
    }
}
