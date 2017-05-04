package com.yuntian.youth.dynamic.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.My.service.MyService;
import com.yuntian.youth.dynamic.api.GDLBSApi;
import com.yuntian.youth.dynamic.model.CreateResults;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.service.ReleaseService;
import com.yuntian.youth.dynamic.view.callback.ReleaseView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.RxSubscribe;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.ProgressCallback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;


/**
 * Created by huxianguang on 2017/4/25.
 */

public class ReleasePresenter extends MvpBasePresenter<ReleaseView> {
    private String mLbsid;
    private BmobFile mBmobFile;

    public void send(final String content, String coordinates, final Context context, final String path, final Boolean isAnonymous) {
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
                .all(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        mLbsid = s;
                        if (TextUtils.isEmpty(path)){
                            return false;
                        }
                        return true;
                    }
                })
                .flatMap(new Func1<Boolean, Observable<File>>() {
                    @Override
                    public Observable<File> call(Boolean aBoolean) {
                        if (aBoolean){
                            return Luban.get(context).load(new File(path)).putGear(Luban.THIRD_GEAR).asObservable();
                        }
                        File file=null;
                        return Observable.just(file);
                    }
                })
                .flatMap(new Func1<File, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(File file) {
                        if (file!=null){
                            mBmobFile = new BmobFile(file);
                            Observable<Void> observable=mBmobFile.uploadObservable(new ProgressCallback() {
                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                            return observable;
                        }
                        Void v=null;
                        return Observable.just(v);
                    }
                })
                .flatMap(new Func1<Void, Observable<String>>() {
                    @Override
                    public Observable<String> call(Void aVoid) {
                        //创建dynamic数据
                        String uri=null;
                        if (mBmobFile!=null){
                            uri=mBmobFile.getFileUrl();
                        }
                        return ReleaseService.createDynamic(content,uri,isAnonymous);
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
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        if (mBmobFile!=null){
                            return MyService.saveFile(mBmobFile.getFileUrl(),mBmobFile.getUrl());
                        }
                        return Observable.just("1");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscribe<String>(context, "提交中") {
                    @Override
                    protected void _onNext(String s) {
                        if ("1".equals(s)){
                            //文件保存失败
                        }
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

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        Log.v("=======","onCompleted");
                    }
                });
    }
}
