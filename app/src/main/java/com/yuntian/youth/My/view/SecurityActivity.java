package com.yuntian.youth.My.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.My.presenter.SecurityPresenter;
import com.yuntian.youth.My.view.callback.SecurityView;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.StringUtils;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.view.CheckActivity;
import com.yuntian.youth.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huxianguang on 2017/5/24.
 */

public class SecurityActivity extends MvpActivity<SecurityView,SecurityPresenter> implements SecurityView {

    @BindView(R.id.security_title)
    TitleBar mSecurityTitle;
    @BindView(R.id.security_phone)
    TextView mSecurityPhone;
    @BindView(R.id.security_sendSms)
    Button mSecuritySendSms;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
        initView();

    }

    @NonNull
    @Override
    public SecurityPresenter createPresenter() {
        return new SecurityPresenter(this);
    }

    private void initView() {
        initTitle();
        mSecurityPhone.setText(StringUtils.encryptionPhone(User.getCurrentUser().getMobilePhoneNumber()));
    }

    private void initTitle() {
        mSecurityTitle.setLeftImageResource(R.mipmap.left);
        mSecurityTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSecurityTitle.setTitle("安全验证");
        mSecurityTitle.setTitleColor(Color.WHITE);
    }

    @OnClick(R.id.security_sendSms)
    public void onViewClicked() {
        //发送验证码
        getPresenter().sendSms();
    }

    @Override
    public void Success() {
        //跳到验证页面
        Intent intent=new Intent(this, CheckActivity.class);
        intent.putExtra("phone",User.getCurrentUser().getMobilePhoneNumber());
        intent.putExtra("type", Constant.CODE_PASSWORD);
        startActivity(intent);
    }

    @Override
    public void Erro(String erro) {
        Toast.makeText(this,erro,Toast.LENGTH_LONG).show();
    }
}
