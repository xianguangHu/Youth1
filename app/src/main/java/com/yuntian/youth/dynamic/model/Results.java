package com.yuntian.youth.dynamic.model;

/**
 * Created by huxianguang on 2017/4/25.
 * 高德LBS云创建数据返回结果
 */

public class Results {
    private String info;//错误码说明 成功返回OK
    private int status;//返回状态 1成功  0失败
    private String infocode; //错误码
    private String _id;//返回成功的id

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
