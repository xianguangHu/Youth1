package com.yuntian.youth.My.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yuntian.youth.My.model.File;
import com.yuntian.youth.Utils.DialogUtil;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.view.RegisterActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import rx.Observable;

/**
 * Created by huxianguang on 2017/4/23.
 */

public class MyService {

    /**
     *
     * @param uri 文件下载的uri
     * @return
     */
    public static Observable<Void> updateUserHead(String uri){
        User user=new User();
        user.setHeadUri(uri);
        Observable<Void> observable=user.updateObservable(BmobUser.getCurrentUser().getObjectId());
        return observable;
    }

    /**
     * 将文件信息保存到file表
     * @param uri
     * @param deleteUri
     * @return
     */
    public static Observable<String> saveFile(String uri,String deleteUri){
        File file=new File();
        file.setDeleteUri(deleteUri);
        file.setUri(uri);
        Observable<String> observable=file.saveObservable();
        return observable;
    }

    /**
     *  查询是否有头像
     * @return
     */
    public static Observable<List<File>> QueryHead(){
        User user=BmobUser.getCurrentUser(User.class);
        if (user.getHeadUri()!=null){
            //已经上传过头像  先删除
            BmobQuery<File> query=new BmobQuery<>();
            query.addWhereEqualTo("uri",user.getHeadUri());
            Observable<List<File>> observable=query.findObjectsObservable(File.class);
            return observable;
        }
        List<File> list=null;
        return Observable.just(list);
    }

    public static void deleteHead(final List<File> list){
        String deleteUri=list.get(0).getDeleteUri();
        if (deleteUri!=null){
            BmobFile file=new BmobFile();
            file.setUrl(deleteUri);
            file.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    //bmob文件删除后 还要删除自己建的File表中的文件数据
                    list.get(0).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Log.v("===","删除成功");
                        }
                    });
                }
            });
        }
    }

    /**
     * 退出登陆
     * @param context
     * @param type
     */
    public static void logOut(Context context,int type){
        if (Constant.LOG_OUT_SHOW==type){//需要显示dialog
            DialogUtil.showNormalDialog(context);
            return;
        }
        BmobUser.logOut();
        context.startActivity(new Intent(context, RegisterActivity.class));

    }
}
