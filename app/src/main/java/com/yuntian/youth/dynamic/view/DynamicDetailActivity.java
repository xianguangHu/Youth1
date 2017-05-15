package com.yuntian.youth.dynamic.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by huxianguang on 2017/5/8.
 */

public class DynamicDetailActivity extends FragmentActivity{


    @BindView(R.id.dynamic_detail_title)
    TitleBar mDynamicDetailTitle;
    private Dynamic mDynamic;
    private String mLocationDistance;//距离多少米
    private boolean mIsOpenBoard;//是否打开键盘

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        ButterKnife.bind(this);
        mIsOpenBoard = getIntent().getBooleanExtra("isOpenBoard", false);
        mDynamic = (Dynamic) getIntent().getSerializableExtra("detail");
        mLocationDistance = getIntent().getStringExtra("locationDistance");
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
        initView();
        getSupportFragmentManager().beginTransaction().replace(R.id.dynamic_detail_fragment, new DetailListFragment()).commitAllowingStateLoss();
    }

    private void initView() {
        initTitle();
    }
    private void initTitle() {

        mDynamicDetailTitle.setLeftImageResource(R.mipmap.left);
        mDynamicDetailTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDynamicDetailTitle.setTitle("详情");
        mDynamicDetailTitle.setTitleColor(Color.WHITE);
    }

    public boolean getIsOpenBoard(){
        return mIsOpenBoard;
    }

    public Dynamic getDynamicData(){
        return mDynamic;
    }

    public String getLocationDistance(){
        return mLocationDistance;
    }
}
