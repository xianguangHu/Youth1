package com.yuntian.youth.My.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.My.presenter.ReplaceNumberPresenter;
import com.yuntian.youth.My.view.callback.ReplaceNumberView;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huxianguang on 2017/5/24.
 */

public class ReplaceNunberActivity extends MvpActivity<ReplaceNumberView,ReplaceNumberPresenter> implements ReplaceNumberView {
    @BindView(R.id.replace_number_title)
    TitleBar mReplaceNumberTitle;
    @BindView(R.id.replace_number_editview)
    EditText mReplaceNumberEditview;
    @BindView(R.id.replace_number_next)
    Button mReplaceNumberNext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_number);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
        initView();
    }

    @NonNull
    @Override
    public ReplaceNumberPresenter createPresenter() {
        return new ReplaceNumberPresenter(this);
    }

    private void initView() {
        initTitle();
    }

    private void initTitle() {
        mReplaceNumberTitle.setLeftImageResource(R.mipmap.left);
        mReplaceNumberTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mReplaceNumberTitle.setTitle("更换手机号码");
        mReplaceNumberTitle.setTitleColor(Color.WHITE);
    }

    @OnClick(R.id.replace_number_next)
    public void onViewClicked() {
        String number=mReplaceNumberEditview.getText().toString().trim();
        getPresenter().sendSMS(number);
    }

    @Override
    public void Success(String phone) {
        //已向号码发送验证码  跳到验证页
    }

    @Override
    public void Erro(String erro) {
        Toast.makeText(this,erro,Toast.LENGTH_SHORT).show();
    }
}
