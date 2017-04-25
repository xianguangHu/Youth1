package com.yuntian.youth.Utils;

import android.app.Activity;
import android.app.ProgressDialog;

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
}
