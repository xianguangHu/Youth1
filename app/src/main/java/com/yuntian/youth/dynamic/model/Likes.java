package com.yuntian.youth.dynamic.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by huxianguang on 2017/5/5.
 */

public class Likes extends BmobObject{
    public Likes(){
        setTableName("Likes");
    }
    private String userId;
    private String dynamicId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }
}
