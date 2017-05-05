package com.yuntian.youth.dynamic.presenter;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.Utils.LoctionUtils;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.DynamicDateil;
import com.yuntian.youth.dynamic.model.Dynamic_Location;
import com.yuntian.youth.dynamic.service.DynamicService;
import com.yuntian.youth.dynamic.service.GDReieveService;
import com.yuntian.youth.dynamic.view.callback.DynamicView;
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

    public void addLike(DynamicDateil dynamicDateil){
        //将dynamic表中的likes加1
        Dynamic dynamic=dynamicDateil.getDynamic();
        if (dynamic!=null){
            DynamicService.addLike(dynamic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            //UI加一
                            Log.v("======","jia1");
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }
    }
}
