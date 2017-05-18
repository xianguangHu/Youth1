package com.yuntian.youth.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by huxianguang on 2017/4/19.
 */

public class DialogUtil {
    public static ProgressDialog showSpinnerDialog(Activity activity){
        ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setMessage("努力加载中...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        //点击外面不消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }


    public static void showChoosePicDialog(Context context, String[] item, String title, final showDialogCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setNegativeButton("取消", null);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.onClick(dialog,which);
            }
        });
        builder.create().show();
    }
    public interface showDialogCallBack{
        void onClick(DialogInterface dialog, int which);
    }
}
