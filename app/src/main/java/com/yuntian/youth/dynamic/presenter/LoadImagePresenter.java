package com.yuntian.youth.dynamic.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.yuntian.youth.dynamic.view.callback.LoadImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huxianguang on 2017/5/17.
 */

public class LoadImagePresenter extends MvpBasePresenter<LoadImageView>{
    private Context mContext;
    private File mCurrentFile;

    public LoadImagePresenter(Context context){
        mContext=context;
    }

    /**
     * 保存图片到本地
     * @param uri
     */
    public void savePhotoToSystem(final String uri){
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Bitmap bitmap=null;
                try {
                    bitmap= Glide.with(mContext)
                            .load(uri)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    if (bitmap!=null){
                        //在这里执行保存图片的方法
                        saveImageToGallery(bitmap,subscriber);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e));
                }finally {
                    if (bitmap!=null&&mCurrentFile.exists()){
                        subscriber.onNext(1);
                    }else {
                        subscriber.onError(new Throwable("保存失败"));
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                getView().onDownLoadSuccess();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                getView().onDownLoadErro();

            }
        });
    }

    /**
     * 执行保存
     * @param bitmap
     */
    private void saveImageToGallery(Bitmap bitmap,Subscriber<? super Integer> subscriber) {
        //首先保存图片  注意小米手机必须这样获得public绝对路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        String fileName="新建文件夹";
        File appDir=new File(file,fileName);
        if (!appDir.exists()){
            appDir.mkdirs();
        }
        String photoName=System.currentTimeMillis() + ".png";
        mCurrentFile = new File(appDir,photoName);
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(mCurrentFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(new Throwable(e));
        }finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 最后通知图库更新
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(mCurrentFile.getPath()))));
    }
}
