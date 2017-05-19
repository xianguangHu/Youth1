package com.yuntian.youth.dynamic.model;

import android.os.Parcel;

import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.core.LatLonPoint;

import java.io.Serializable;

/**
 * Created by huxianguang on 2017/5/18.
 */

public class MyCloudItem extends CloudItem implements Serializable{
    public MyCloudItem(String s, LatLonPoint latLonPoint, String s1, String s2) {
        super(s, latLonPoint, s1, s2);
    }

    protected MyCloudItem(Parcel parcel) {
        super(parcel);
    }
}
