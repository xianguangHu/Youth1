package com.yuntian.youth.dynamic.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by huxianguang on 2017/4/25.
 */

public class Dynamic_Location extends BmobObject{
    public Dynamic_Location(){
        this.setTableName("Dynamic_Location");
    }

    private String lbsId;
    private Dynamic dynamic;

    public String getLbsId() {
        return lbsId;
    }

    public void setLbsId(String lbsId) {
        this.lbsId = lbsId;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }
}
