package com.yuntian.youth.dynamic.model;

import com.amap.api.services.cloud.CloudItem;

/**
 * Created by huxianguang on 2017/5/3.
 */

public class DynamicDateil {
    private Dynamic dynamic;
    private CloudItem cloudItem;

    private boolean isLike;//判断用户是否已经点过赞  false没有点过  true点过

    private boolean isUnLike;//判断用户是否已经点过睬  false没有点过  true点过

    public boolean isUnLike() {
        return isUnLike;
    }

    public void setUnLike(boolean unLike) {
        isUnLike = unLike;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public CloudItem getCloudItem() {
        return cloudItem;
    }

    public void setCloudItem(CloudItem cloudItem) {
        this.cloudItem = cloudItem;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }


}
