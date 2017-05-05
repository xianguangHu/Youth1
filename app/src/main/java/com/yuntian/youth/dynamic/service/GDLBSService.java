package com.yuntian.youth.dynamic.service;

import com.google.gson.JsonObject;
import com.yuntian.youth.dynamic.model.Results;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/25.
 * 高德地图LBS云储存
 */

public interface GDLBSService {
    @FormUrlEncoded
    @POST("/datamanage/data/create")
    Observable<Results> addLocation(@Field("key") String key,
                                    @Field("tableid") String tableid,
                                    @Field("loctype") int loctype,
                                    @Field("data") JsonObject data);

    @FormUrlEncoded
    @POST("/datamanage/data/update")
    Observable<Results> updateLikes(@Field("key") String key,
                           @Field("tableid") String tableid,
                           @Field("data") JsonObject data);
}
