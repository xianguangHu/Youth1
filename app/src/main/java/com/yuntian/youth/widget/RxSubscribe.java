package com.yuntian.youth.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.yuntian.youth.Utils.NetWorkUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;


/**
 * rxjava dialog封装
 * Created by huxianguang on 2017/4/25.
 */

public abstract class RxSubscribe<T> extends Subscriber<T> {
    private Context mContext;
    private SweetAlertDialog dialog;
    private String msg;

    protected boolean showDialog() {
        return true;
    }

    /**
     * @param context context
     * @param msg     dialog message
     */
    public RxSubscribe(Context context, String msg) {
        this.mContext = context;
        this.msg = msg;
    }

    /**
     * @param context context
     */
    public RxSubscribe(Context context) {
        this(context, "请稍后...");
    }

    @Override
    public void onCompleted() {
        if (showDialog())
            dialog.dismiss();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (showDialog()) {
            dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(msg);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if(!isUnsubscribed()){
                        unsubscribe();
                    }
                }
            });
            dialog.show();
        }
    }
    @Override
    public void onNext(T t) {
        _onNext(t);
        dialog.dismiss();
    }
    @Override
    public void onError(Throwable e) {
        if (!NetWorkUtils.isAvailable(mContext)){
            Toast.makeText(mContext,"网络不可用",Toast.LENGTH_SHORT).show();
            _onError("网络不可用");
            return;
        }
        e.printStackTrace();
        _onError(e.getMessage());
        if (showDialog())
            dialog.dismiss();
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
