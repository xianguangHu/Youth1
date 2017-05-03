package com.yuntian.youth.dynamic.service;

import com.yuntian.youth.dynamic.model.Dynamic_Location;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/28.
 */

public class DynamicService {
    public static Observable<List<Dynamic_Location>> QueryLbsToDynamic(String LbsId){
        BmobQuery<Dynamic_Location> bmobQuery=new BmobQuery();
        bmobQuery.addWhereEqualTo("lbsId",LbsId);
        bmobQuery.include("dynamic");
        Observable<List<Dynamic_Location>> observable=bmobQuery.findObjectsObservable(Dynamic_Location.class);
        return observable;
    }
}
