package com.yuntian.youth.My.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huxianguang on 2017/5/23.
 */

public class UpdatePhoneActivity extends Activity {
    @BindView(R.id.update_phone_title)
    TitleBar mUpdatePhoneTitle;
    @BindView(R.id.update_phone_number)
    TextView mUpdatePhoneNumber;
    @BindView(R.id.update_phone_btnupdate)
    Button mUpdatePhoneBtnupdate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
        initView();
    }

    private void initView() {
        initTitle();
        User user=User.getCurrentUser();
        String number=user.getMobilePhoneNumber();
        String str1=number.substring(0,3);
        String str2=number.substring(number.length()-2);
        String newNumber=str1+" ****** "+str2;
        mUpdatePhoneNumber.setText(newNumber);
    }

    private void initTitle() {
        mUpdatePhoneTitle.setLeftImageResource(R.mipmap.left);
        mUpdatePhoneTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUpdatePhoneTitle.setTitle("我的手机号");
        mUpdatePhoneTitle.setTitleColor(Color.WHITE);
    }

    @OnClick(R.id.update_phone_btnupdate)
    public void onViewClicked() {
        startActivity(new Intent(this,ReplaceNunberActivity.class));
    }
}
