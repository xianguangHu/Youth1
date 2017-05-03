package com.yuntian.youth.dynamic.service;

import android.content.Context;

import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;

/**
 * Created by huxianguang on 2017/4/26.
 */

public class GDReieveService {
    public static void QueryNear(Context context,Double longitude,Double latitude,CloudSearch.OnCloudSearchListener onCloudSearchListener) throws AMapException {
        CloudSearch cloudSearch=null;
        if (cloudSearch==null){
            cloudSearch=new CloudSearch(context);
            cloudSearch.setOnCloudSearchListener(onCloudSearchListener);
        }
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(
                latitude, longitude), 2000);
        CloudSearch.Query query=new CloudSearch.Query("58feb01b2376c11620d1ce62","",bound);
        cloudSearch.searchCloudAsyn(query);
    }

//    static class MyCloudSearchListener implements CloudSearch.OnCloudSearchListener{
//
//        @Override
//        public void onCloudSearched(CloudResult cloudResult, int i) {
//            Log.v("返回数量",cloudResult.getTotalCount()+"");
//        }
//
//        @Override
//        public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {
//
//        }
//    }
}
