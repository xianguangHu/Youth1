package com.yuntian.youth.register.model.bean;

import com.google.gson.annotations.SerializedName;

import cn.bmob.v3.BmobUser;

/**
 * Created by huxianguang on 2017/4/15.
 */

public class User extends BmobUser {
//    @SerializedName("username")
//    private String userName;
////    @SerializedName("mobilePhoneNumber")
////    private String phone;
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }

//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
    @SerializedName("headUri")
    private String headUri;


    public String getHeadUri() {
        if (headUri == null) {
            return "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493033458035&di=7828d59abef7f5106050c5c9cfa562b4&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2015%2F159%2F05%2F38L4VOK58LX0.jpg";
        } else {
            return headUri;
        }
    }

    public void setHeadUri(String headUri) {
        this.headUri = headUri;
    }

    public static User getCurrentUser(){
        return BmobUser.getCurrentUser(User.class);
    }

}
