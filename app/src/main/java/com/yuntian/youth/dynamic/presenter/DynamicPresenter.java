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
import com.yuntian.youth.Utils.CacheDataUtils;
import com.yuntian.youth.Utils.LoctionUtils;
import com.yuntian.youth.dynamic.api.GDLBSApi;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.DynamicDateil;
import com.yuntian.youth.dynamic.model.Dynamic_Location;
import com.yuntian.youth.dynamic.model.Likes;
import com.yuntian.youth.dynamic.model.Results;
import com.yuntian.youth.dynamic.service.DynamicService;
import com.yuntian.youth.dynamic.service.GDReieveService;
import com.yuntian.youth.dynamic.view.callback.DynamicView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/4/27.
 */

public class DynamicPresenter extends MvpBasePresenter<DynamicView> {
    private Context mContext;
    private List<DynamicDateil> mDynamicDateils=new ArrayList<>();

    public DynamicPresenter(Context context){
        mContext=context;
    }

    //LBS本页总数
    private int mNumber;
    //索引
    private int index = 0;
    private CloudResult mCloudResult;

    /**
     * 获取数据
     * @param type 属性 DYNAMIC_LOAD刷新加载 不需要分页
     *             DYNAMIC_LOAD_MORE 上拉刷新加载更多 需要分页
     */
    public void getData(final int type) {
        mDynamicDateils.clear();
        //mmb
        Observable.create(new Observable.OnSubscribe<AMapLocation>() {
            //获取定位信息
            @Override
            public void call(final Subscriber<? super AMapLocation> subscriber) {
                LoctionUtils.getLocation(new LoctionUtils.MyLocationListener() {
                    @Override
                    public void result(AMapLocation location) {
                        if (location != null) {
                            subscriber.onNext(location);
                            subscriber.onCompleted();
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
                                    GDReieveService.QueryNear(mContext, mapLocation.getLongitude(), mapLocation.getLatitude(), new CloudSearch.OnCloudSearchListener() {
                                        @Override
                                        public void onCloudSearched(CloudResult cloudResult, int e) {
                                            //e为错误码  返回1000表示正常返回
                                            if (e == 1000) {
                                                mCloudResult = cloudResult;
                                                mNumber = cloudResult.getClouds().size();
                                                subscriber.onNext(cloudResult.getClouds());
                                                subscriber.onCompleted();
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
                .flatMap(new Func1<CloudItem, Observable<DynamicDateil>>() {
                    @Override
                    public Observable<DynamicDateil> call(CloudItem cloudItem) {
                        //通过zip将数据封装好
                        return Observable.zip(Observable.just(cloudItem), DynamicService.QueryLbsToDynamic(cloudItem.getID()), new Func2<CloudItem, List<Dynamic_Location>, DynamicDateil>() {
                            @Override
                            public DynamicDateil call(CloudItem cloudItem, List<Dynamic_Location> dynamic_locaions) {
                                DynamicDateil dynamicDateil=new DynamicDateil();
                                dynamicDateil.setDynamic(dynamic_locaions.get(0).getDynamic());
                                dynamicDateil.setCloudItem(cloudItem);
                                return dynamicDateil;
                            }
                        });
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DynamicDateil>() {
                    @Override
                    public void onCompleted() {
                        getView().update2loadData(mDynamicDateils,type);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("===erro",e.getMessage());
                    }

                    @Override
                    public void onNext(DynamicDateil dynamicDateil) {
                        mDynamicDateils.add(dynamicDateil);
                    }
                });
    }

    /**
     * 点赞
     *
     * @param dynamicDateil
     * @param position
     */
    public void addLike(final int type, final DynamicDateil dynamicDateil, final int position) {
        String json = "{'_id':'" + dynamicDateil.getCloudItem().getID() + "','likes':" + dynamicDateil.getDynamic().getLikes() + "}";
        Log.v("======", json);
        final Dynamic dynamic = dynamicDateil.getDynamic();
        final JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        //将dynamic表中的likes跟新
        GDLBSApi.updateLikes(Constant.GDLBS_KEY, Constant.GDYUN_ID, data)
                .flatMap(new Func1<Results, Observable<List<Likes>>>() {
                    @Override
                    //查询
                    public Observable<List<Likes>> call(Results results) {
                        return DynamicService.QueryLikes(dynamic.getObjectId());
                    }
                })
                .flatMap(new Func1<List<Likes>, Observable<?>>() {
                    //删除
                    @Override
                    public Observable<?> call(List<Likes> likes) {
                        if (likes.size()> 0) {
                            return DynamicService.DeleteLikes(likes.get(0).getObjectId());
                        }
                        return Observable.just(1);
                    }
                })
                //添加  原子计数器
                .flatMap(new Func1<Object, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(Object o) {
                        return DynamicService.addLike(dynamic, type);
                    }
                })
                //添加到likes表
                .flatMap(new Func1<Void, Observable<String>>() {
                    @Override
                    public Observable<String> call(Void aVoid) {
                        //将数据保存到点赞表
                        return DynamicService.saveLikes(dynamicDateil.getDynamic().getObjectId(), type);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.v("========", "保存到数据库");
                        getView().updateLike(position);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.v("erro=====", throwable.getMessage());

                    }
                });

    }


    /**
     * 保存缓存
     * @param dynamicDateils
     */
    public void saveDynamicCachedata(final List<DynamicDateil> dynamicDateils){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                List<Dynamic> dynamics=new ArrayList<Dynamic>();
                if (dynamicDateils.size()>10){
                    for (int i=0;i<10;i++){
                        dynamics.add(dynamicDateils.get(i).getDynamic());
                    }
                }else {
                    for (DynamicDateil dynamicDateil:dynamicDateils){
                        dynamics.add(dynamicDateil.getDynamic());
                    }
                }
                CacheDataUtils.saveRecentDynamic(User.getCurrentUser().getObjectId(),dynamics,mContext);
                subscriber.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.v("====","保存成功");
                    }
                });
    }

    /**
     * 在打开的时候显示上一次的记录
     */
    public void getDynamicCacheDate(final Context context){
        Observable.create(new Observable.OnSubscribe<List<DynamicDateil>>() {
            @Override
            public void call(Subscriber<? super List<DynamicDateil>> subscriber) {
                List<Dynamic> dynamics=CacheDataUtils.getRecentDyanmic(User.getCurrentUser().getObjectId(),context);
                List<DynamicDateil> dynamicDateils=new ArrayList<DynamicDateil>();
                for (Dynamic dynamic:dynamics){
                    DynamicDateil dynamicDateil=new DynamicDateil();
                    dynamicDateil.setDynamic(dynamic);
                    dynamicDateils.add(dynamicDateil);
                }
                subscriber.onNext(dynamicDateils);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DynamicDateil>>() {
                    @Override
                    public void call(List<DynamicDateil> dynamicList) {
                        getView().getCacheSuccess(dynamicList);
                    }
                });
    }
}
