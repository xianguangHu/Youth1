package com.yuntian.youth.My.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by huxianguang on 2017/4/24.
 */

public class File extends BmobObject {
    public File(){
        this.setTableName("File");
    }
    private String deleteUri;//删除的uri
    private String uri;//文件下载的uri

    public String getDeleteUri() {
        return deleteUri;
    }

    public void setDeleteUri(String deleteUri) {
        this.deleteUri = deleteUri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
