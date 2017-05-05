package com.yuntian.youth.dynamic.model;

import com.yuntian.youth.register.model.bean.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by huxianguang on 2017/4/25.
 */

public class Dynamic extends BmobObject {
    public Dynamic(){
        this.setTableName("Dynamic");
    }
    private String content;

    private User user;

    private String photoUri;
    private boolean isAnonymous;//匿名
    private Integer likes;

    public Integer getLikes() {
        if (likes==null){
            return 0;
        }
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
