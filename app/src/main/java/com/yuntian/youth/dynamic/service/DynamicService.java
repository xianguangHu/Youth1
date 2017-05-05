package com.yuntian.youth.dynamic.service;

import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.Dynamic_Location;

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
        bmobQuery.include("dynamic,dynamic.user");
        Observable<List<Dynamic_Location>> observable=bmobQuery.findObjectsObservable(Dynamic_Location.class);
        return observable;
    }

    /**
     *  点赞
     * @param dynamic
     * @return
     */
    public static Observable<Void> addLike(Dynamic dynamic){
        dynamic.increment("likes");
        Observable<Void> observable=dynamic.updateObservable(dynamic.getObjectId());
        return observable;
    }
}
