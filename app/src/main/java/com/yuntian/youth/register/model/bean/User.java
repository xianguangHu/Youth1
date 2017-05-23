package com.yuntian.youth.register.model.bean;

import com.google.gson.annotations.SerializedName;
import com.yuntian.youth.global.Constant;

import cn.bmob.v3.BmobUser;

/**
 * Created by huxianguang on 2017/4/15.
 */

public class User extends BmobUser {

    @SerializedName("headUri")
    private String headUri;

    @SerializedName("gender")
    private Integer gender;

    @SerializedName("age")
    private String age;

    @SerializedName("birthday")
    private String birthday;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        if (age!=null) {
            return age;
        }
        return "未填写";
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        if (gender != null) {
            if (Constant.GEBDER_MAN == gender) {
                return "男";
            } else if (Constant.GENDER_WOMAN == gender) {
                return "女";
            }
        }
        return "未填写";
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

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

    public static User getCurrentUser() {
        return BmobUser.getCurrentUser(User.class);
    }

}
