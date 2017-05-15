package com.yuntian.youth.dynamic.service;

import android.util.Log;

import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.Dynamic_Location;
import com.yuntian.youth.dynamic.model.Likes;
import com.yuntian.youth.register.model.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/28.
 */

public class DynamicService {
    /**
     *  通过lbsid 来查询dynamic
     * @param LbsId
     * @return
     */
    public static Observable<List<Dynamic_Location>> QueryLbsToDynamic(String LbsId){
        BmobQuery<Dynamic_Location> bmobQuery=new BmobQuery();
        bmobQuery.addWhereEqualTo("lbsId",LbsId);
        bmobQuery.include("dynamic.user");
        Observable<List<Dynamic_Location>> observable=bmobQuery.findObjectsObservable(Dynamic_Location.class);
        return observable;
    }

    /**
     *  点赞
     * @param dynamic
     * @param type 点赞的个数
     * @return
     */
    public static Observable<Void> addLike(Dynamic dynamic,int type){
        dynamic.increment("likes",type);
        Observable<Void> observable=dynamic.updateObservable(dynamic.getObjectId());
        return observable;
    }

    /**
     * 将点赞人和动态的信息保存到likes表中
     * @param dynamicId
     * @param type 点赞数
     * @return
     */
    public static Observable<String> saveLikes(String dynamicId,int type){
        Likes likes=new Likes();
            Log.v("======",User.getCurrentUser().getObjectId());
        likes.setUserId(User.getCurrentUser().getObjectId());
        likes.setDynamicId(dynamicId);
        Observable<String> observable=likes.saveObservable();
        return observable;
    }

    /**
     * 查询likes
     * @param dynamicId
     * @return
     */
    public static Observable<List<Likes>> QueryLikes(String dynamicId){
        BmobQuery<Likes> query=new BmobQuery<>();
        query.addWhereEqualTo("dynamicId",dynamicId);
        query.addWhereEqualTo("userId",User.getCurrentUser().getObjectId());
        Observable<List<Likes>> observable=query.findObjectsObservable(Likes.class);
        return observable;
    }

    /**
     * 删除likes
     * @param likesId
     * @return
     */
    public static Observable<Void> DeleteLikes(String likesId){
        Likes likes=new Likes();
        likes.setObjectId(likesId);
        return likes.deleteObservable(likesId);
    }
}
