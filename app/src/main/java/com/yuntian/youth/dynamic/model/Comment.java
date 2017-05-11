package com.yuntian.youth.dynamic.model;

import com.yuntian.youth.register.model.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by huxianguang on 2017/5/10.
 */

public class Comment extends BmobObject{
    public Comment(){
        setTableName("Comment");
    }
    private User replyAuthor;//回复评论人的user
    private boolean isReply;//是否是回复类型
    private Dynamic dynamic;//该条评论的动态
    private String content;//评论的内容
    private User author;//评论动态的用户

    public User getReplyAuthor() {
        return replyAuthor;
    }

    public void setReplyAuthor(User replyAuthor) {
        this.replyAuthor = replyAuthor;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
