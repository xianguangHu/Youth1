package com.yuntian.youth.register.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.DialogUtil;
import com.yuntian.youth.mian.view.MainActivity;
import com.yuntian.youth.register.presenter.RegisterPresenter;
import com.yuntian.youth.register.view.callback.RegisterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {
    @BindView(R.id.Register_erro)
    TextView mRegisterErro;
    @BindView(R.id.register_phone)
    EditText mRegisterPhone;
    @BindView(R.id.register_go)
    Button mRegisterGo;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        if (BmobUser.getCurrentUser()!=null){
            startActivity(new Intent(this, MainActivity.class));
        }
        RegisterActivityPermissionsDispatcher.initViewWithCheck(this);
        initView();

    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    void initView() {
        mRegisterPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterErro.setVisibility(View.INVISIBLE);
            }
        });
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }


    //跳转到登陆界面
    @Override
    public void startLogin(String phone) {
        dissDialog(mDialog);
        Intent inten = new Intent(this, LoginActivity.class);
        inten.putExtra("phone", phone);
        startActivity(inten);
    }

    //跳转到注册界面
    @Override
    public void StartRegister(String phone) {
        dissDialog(mDialog);
        Intent inten = new Intent(this, CheckActivity.class);
        inten.putExtra("phone", phone);
        startActivity(inten);
    }

    @Override
    public void RegisterErro(String e) {
        dissDialog(mDialog);
        mRegisterErro.setText(e);
        mRegisterErro.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.register_go)
    public void onViewClicked(View view) {
        Log.v("registerActivity", "onclick");
        mDialog = DialogUtil.showSpinnerDialog(this);
        getPresenter().go(mRegisterPhone.getText().toString().trim());
    }

    public void dissDialog(ProgressDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    void showRationaleForRecord(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    void showRecordDenied() {
        Toast.makeText(this, "拒绝录音权限将无法进行挑战", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void onRecordNeverAskAgain() {
        new AlertDialog.Builder(this)
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2016/11/10 打开系统设置权限
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("您已经禁止了录音权限,是否现在去开启")
                .show();
    }
}
