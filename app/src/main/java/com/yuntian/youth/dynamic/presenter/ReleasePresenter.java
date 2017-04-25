package com.yuntian.youth.dynamic.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.dynamic.api.GDLBSApi;
import com.yuntian.youth.dynamic.model.CreateResults;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.service.ReleaseService;
import com.yuntian.youth.dynamic.view.callback.ReleaseView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.RxSubscribe;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by huxianguang on 2017/4/25.
 */

public class ReleasePresenter extends MvpBasePresenter<ReleaseView> {
    private String mLbsid;

    public void send(final String content, String coordinates, Context context) {
        String json = "{'_name':'" + User.getCurrentUser().getUsername() + "','_location':'" + coordinates + "'}";
        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        //将经纬度信息保存到高德LBS云图中
        GDLBSApi.add(Constant.GDLBS_KEY, Constant.GDYUN_ID, Constant.GDLOCTYPE, data)
                .flatMap(new Func1<CreateResults, Observable<String>>() {
                    @Override
                    public Observable<String> call(CreateResults createResults) {
                        if (0 == createResults.getStatus()) {//错误
                            return Observable.error(new Throwable("info:" + createResults.getInfo() + "+infocode:" + createResults.getInfocode()));
                        }

                        return Observable.just(createResults.get_id());
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        mLbsid = s;
                        //创建dynamic数据
                        return ReleaseService.createDynamic(content);
                    }
                })
                .flatMap(new Func1<String, Observable<Dynamic>>() {
                    @Override
                    public Observable<Dynamic> call(String dynamicId) {
                        //获取到dynamic的id通过查询获取dynamic对象
                        return ReleaseService.QueryDynamic(dynamicId);
                    }
                })
                .flatMap(new Func1<Dynamic, Observable<String>>() {
                    @Override
                    public Observable<String> call(Dynamic dynamic) {
                        //保存dynamic与location的中间表信息
                        return ReleaseService.createDynamicLocation(mLbsid, dynamic);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<String>(context, "提交中") {
                    @Override
                    protected void _onNext(String s) {
                        //成功
                        if (getView() != null) {
                            getView().sendSuccess();
                        }

                    }

                    @Override
                    protected void _onError(String message) {
                        if (getView() != null) {
                            getView().sendErro(message);
                        }
                    }
                });
    }
}
