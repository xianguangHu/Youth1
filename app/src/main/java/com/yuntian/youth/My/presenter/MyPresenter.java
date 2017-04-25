package com.yuntian.youth.My.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.My.service.MyService;
import com.yuntian.youth.My.view.callback.MyView;
import com.yuntian.youth.Utils.ImageUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.ProgressCallback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/4/23.
 */

public class MyPresenter extends MvpBasePresenter<MyView> {

    private BmobFile mBmobFile;

    public void uploadHead(Bitmap[] bitmap, final Context context) {
        Observable.from(bitmap)
                .map(new Func1<Bitmap, String>() {

                    @Override
                    public String call(Bitmap bitmap) {
                        String path = ImageUtil.savePhoto(bitmap, context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Bmob", BmobUser.getCurrentUser().getMobilePhoneNumber()+"_" + System.currentTimeMillis());
                        Log.v("======",path);
                        return path;
                    }
                })
                .flatMap(new Func1<String, Observable<Void>>() {
                    @Override
                    public Observable call(String s) {
                        //上传
                        mBmobFile = new BmobFile(new File(s));
                        Observable<Void> observable = mBmobFile.uploadObservable(new ProgressCallback() {
                            @Override
                            public void onProgress(Integer integer, long l) {
                            }
                        });
                        return observable;
                    }
                })
                .flatMap(new Func1<Void, Observable<List<com.yuntian.youth.My.model.File>>>() {
                    @Override
                    public Observable<List<com.yuntian.youth.My.model.File>> call(Void aVoid) {
                        //查询是否已经上传过头像
                        return MyService.QueryHead();
                    }
                })
                .flatMap(new Func1<List<com.yuntian.youth.My.model.File>, Observable<Void>>() {
                        @Override
                        public Observable<Void> call(List<com.yuntian.youth.My.model.File> list) {
                            //将文件uri更新到用户表中
                            if (list!=null){
                                //删除文件
                                MyService.deleteHead(list);
                            }
                            return MyService.updateUserHead(mBmobFile.getFileUrl());
                        }
                    })
                .flatMap(new Func1<Void, Observable<String>>() {
                    @Override
                    public Observable<String> call(Void aVoid) {
                        //将文件信息保存到文件表
                        return MyService.saveFile(mBmobFile.getFileUrl(),mBmobFile.getUrl());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String string) {
                        //头像更新成功 将头像Uri返回
                        if(getView()!=null){
                            getView().UpdateHeadSuccess(mBmobFile.getFileUrl());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //失败 将错误信息返回
                        if(getView()!=null){
                            getView().Erro(throwable.getMessage());
                        }
                    }
                });

    }
}
