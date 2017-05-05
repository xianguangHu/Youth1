package com.yuntian.youth.dynamic.presenter;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.Utils.LoctionUtils;
import com.yuntian.youth.dynamic.api.GDLBSApi;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.DynamicDateil;
import com.yuntian.youth.dynamic.model.Dynamic_Location;
import com.yuntian.youth.dynamic.model.Results;
import com.yuntian.youth.dynamic.service.DynamicService;
import com.yuntian.youth.dynamic.service.GDReieveService;
import com.yuntian.youth.dynamic.view.callback.DynamicView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.widget.RxSubscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/4/27.
 */

public class DynamicPresenter extends MvpBasePresenter<DynamicView> {

    //LBS本页总数
    private int mNumber;
    //索引
    private int index = 0;
    private CloudResult mCloudResult;

    /**
     * 获取数据
     */
    public void getData(final Context context) {
        //mmb
        Observable.create(new Observable.OnSubscribe<AMapLocation>() {
            @Override
            public void call(final Subscriber<? super AMapLocation> subscriber) {
                LoctionUtils.getLocation(new LoctionUtils.MyLocationListener() {
                    @Override
                    public void result(AMapLocation location) {
                        if (location != null) {
                            subscriber.onNext(location);
                            return;
                        }
                        subscriber.onError(new Throwable("定位失败"));
                    }
                });
            }
        })
                .flatMap(new Func1<AMapLocation, Observable<ArrayList<CloudItem>>>() {
                    @Override
                    public Observable<ArrayList<CloudItem>> call(final AMapLocation mapLocation) {
//                        Log.v("=============",s.getLatitude()+","+s.getLongitude());
                        return Observable.create(new Observable.OnSubscribe<ArrayList<CloudItem>>() {

                            @Override
                            public void call(final Subscriber<? super ArrayList<CloudItem>> subscriber) {
                                try {
                                    GDReieveService.QueryNear(context, mapLocation.getLongitude(), mapLocation.getLatitude(), new CloudSearch.OnCloudSearchListener() {
                                        @Override
                                        public void onCloudSearched(CloudResult cloudResult, int e) {
                                            //e为错误码  返回1000表示正常返回
                                            if (e == 1000) {
                                                mCloudResult = cloudResult;
                                                mNumber = cloudResult.getClouds().size();
                                                subscriber.onNext(cloudResult.getClouds());
                                            } else {
                                                subscriber.onError(new Throwable("云图查询错误码" + e));
                                            }
                                        }

                                        @Override
                                        public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int e) {

                                        }
                                    });
                                } catch (AMapException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                })
                .flatMap(new Func1<ArrayList<CloudItem>, Observable<CloudItem>>() {
                    @Override
                    public Observable<CloudItem> call(ArrayList<CloudItem> cloudItems) {
                        return Observable.from(cloudItems);
                    }
                })
                .concatMap(new Func1<CloudItem, Observable<List<Dynamic_Location>>>() {
                    @Override
                    public Observable<List<Dynamic_Location>> call(CloudItem cloudItem) {
                        //查询中间表数据
                        return DynamicService.QueryLbsToDynamic(cloudItem.getID());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<List<Dynamic_Location>>(context) {
                    List<DynamicDateil> datas = new ArrayList<>();

                    @Override
                    protected void _onNext(List<Dynamic_Location> dynamic_locations) {
                        Log.v("=========", "_onNext");
                        DynamicDateil dynamicDateil = new DynamicDateil();
                        dynamicDateil.setDynamic(dynamic_locations.get(0).getDynamic());
                        dynamicDateil.setCloudItem(mCloudResult.getClouds().get(index));
                        datas.add(dynamicDateil);
                        index++;
                        Log.v("=========", datas.size() + "");
                        if (mNumber == datas.size()) {
                            getView().update2loadData(datas);
                        }

                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }

    public void addLike(final DynamicDateil dynamicDateil, final int position){
        String json="{'_id':'"+dynamicDateil.getCloudItem().getID()+"','likes':"+dynamicDateil.getDynamic().getLikes()+1+"}";
        Log.v("======",json);
        final JsonObject data=new JsonParser().parse(json).getAsJsonObject();
        //将dynamic表中的likes加1
//        GDLBSApi.updateLikes(Constant.GDLBS_KEY,Constant.GDYUN_ID,data)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Results>() {
//                    @Override
//                    public void call(Results results) {
//                        Log.v("======","success");
//
//                    }
//                });
        Dynamic dynamic=dynamicDateil.getDynamic();
        if (dynamic!=null){
            DynamicService.addLike(dynamic)
                    .flatMap(new Func1<Void, Observable<String>>() {
                        @Override
                        public Observable<String> call(Void aVoid) {
                            //将数据保存到点赞表
                            return DynamicService.saveLikes(dynamicDateil.getDynamic().getObjectId());
                        }
                    })
                    .flatMap(new Func1<String, Observable<Results>>() {
                        @Override
                        public Observable<Results> call(String s) {
                            //跟新到LBS云上 赞加一
                            return GDLBSApi.updateLikes(Constant.GDLBS_KEY,Constant.GDYUN_ID,data);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Results>() {
                        @Override
                        public void call(Results results) {
                            Log.v("======","success");
                            if (0!=results.getStatus()){
                                Log.v("======","返回成功");

                            }
                            //UI加一
                            Log.v("======","jia1");
                            if (getView()==null){
                                Log.v("getView=====","null");
                            }
                            getView().updateLike(position);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.v("erro=====",throwable.getMessage());

                        }
                    });
        }
    }
}
