package com.yuntian.youth.dynamic.service;

import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.Dynamic_Location;
import com.yuntian.youth.register.model.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/25.
 */

public class ReleaseService {
    /**
     * 保存动态信息
     * @param content 内容
     * @return
     */
    public static Observable<String> createDynamic(String content,String uri){
        Dynamic dynamic=new Dynamic();
        dynamic.setContent(content);
        dynamic.setUser(BmobUser.getCurrentUser(User.class));
        if (uri!=null){
            dynamic.setPhotoUri(uri);
        }
        Observable<String> observable=dynamic.saveObservable();
        return observable;
    }

    /**
     * 通过id查询到对象
     * @param dynamicId
     * @return
     */
    public static Observable<Dynamic> QueryDynamic(String dynamicId){
        BmobQuery<Dynamic> query=new BmobQuery<>();
        return query.getObjectObservable(Dynamic.class,dynamicId);
    }

    /**
     * 保存中间表信息
     * @param lbsId 高德云图位置id
     * @param dynamic  动态对象
     * @return
     */
    public static Observable<String> createDynamicLocation(String lbsId,Dynamic dynamic){
        Dynamic_Location dynamic_location=new Dynamic_Location();
        dynamic_location.setLbsId(lbsId);
        dynamic_location.setDynamic(dynamic);
        return dynamic_location.saveObservable();

    }

}
