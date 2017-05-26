package com.yuntian.youth.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yuntian.youth.listener.YouthListeren;
import com.yuntian.youth.listener.impl.YouthListerenImpl;
import com.yuntian.youth.register.view.RegisterActivity;

import cn.bmob.v3.BmobUser;

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


    public static void showChoosePicDialog(Context context, String[] item, String title, final YouthListeren listeren) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setNegativeButton("取消", null);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listeren.dialogCall(dialog,which);
            }
        });
        builder.create().show();
    }

    /**
     * 带编辑框的dialog
     * @param context 上下文
     * @param callBack 回调
     */
    public static void showEditDialog(final Context context, final int type, final YouthListerenImpl callBack){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入:");    //设置对话框标题
        final EditText editText=new EditText(context);
        editText.setGravity(View.SCROLL_INDICATOR_TOP);
        builder.setView(editText);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content=editText.getText().toString();
                if (!TextUtils.isEmpty(content)){
                    if (content.length()<10){
                        callBack.dialogCall(content,type);
                        return;
                    }
                    Toast.makeText(context,"长度太长了!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context,"不能为空!",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        AlertDialog dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }

    public static void showNormalDialog(final Context context){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("你确定要退出登陆吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser.logOut();
                        context.startActivity(new Intent(context, RegisterActivity.class));
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }


}
