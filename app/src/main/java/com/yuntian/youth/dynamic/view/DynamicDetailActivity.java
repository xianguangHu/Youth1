package com.yuntian.youth.dynamic.view;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.widget.TitleBar;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by huxianguang on 2017/5/8.
 */

public class DynamicDetailActivity extends Activity {
    @BindView(R.id.dynamic_detail_title)
    TitleBar mDynamicDetailTitle;
    @BindView(R.id.dynamic_detail_head)
    ImageView mDynamicDetailHead;
    @BindView(R.id.dynamic_detail_username)
    TextView mDynamicDetailUsername;
    @BindView(R.id.dynamic_detail_content)
    TextView mDynamicDetailContent;
    @BindView(R.id.dynamic_detail_iv_on)
    ImageView mDynamicDetailIvOn;
    @BindView(R.id.dynamic_detail_likenumber)
    TextView mDynamicDetailLikenumber;
    @BindView(R.id.dynamic_detail_iv_under)
    ImageView mDynamicDetailIvUnder;
    @BindView(R.id.dynamic_detail_likell)
    AutoLinearLayout mDynamicDetailLikell;
    @BindView(R.id.dynamic_detail_photo)
    ImageView mDynamicDetailPhoto;
    @BindView(R.id.dynamic_detail_headll)
    AutoLinearLayout mDynamicDetailHeadll;
    @BindView(R.id.dynamic_detail_time)
    TextView mDynamicDetailTime;
    @BindView(R.id.dynamic_detail_location)
    TextView mDynamicDetailLocation;
    private Dynamic mDynamic;
    private String mLocationDistance;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        ButterKnife.bind(this);
        mDynamic = (Dynamic) getIntent().getSerializableExtra("detail");
        mLocationDistance = getIntent().getStringExtra("locationDistance");
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
        initView();
    }

    private void initView() {
        initTitle();
        //判断是否匿名 匿名就不显示邮箱和username并且影藏 true表示匿名
        if (!mDynamic.isAnonymous()) {
            mDynamicDetailHeadll.setVisibility(View.VISIBLE);
            //设置圆形图片
            Glide.with(this).load(Uri.parse(mDynamic.getUser().getHeadUri()))
                    .bitmapTransform(new CropCircleTransformation(this)).into(mDynamicDetailHead);
            //用户名
            mDynamicDetailUsername.setText(mDynamic.getUser().getUsername());
        } else {
            //匿名了 隐藏头像和username
            mDynamicDetailHeadll.setVisibility(View.GONE);
        }
        mDynamicDetailContent.setText(mDynamic.getContent());
        //显示内容和照片
        mDynamicDetailContent.setText(mDynamic.getContent());
        //判断是否有照片 没有则隐藏
        if (mDynamic.getPhotoUri() != null) {
            mDynamicDetailPhoto.setVisibility(View.VISIBLE);
            Glide.with(this).load(Uri.parse(mDynamic.getPhotoUri()))
                    .bitmapTransform(new CropTransformation(this, 1400, 500, CropTransformation.CropType.CENTER)
                            , new RoundedCornersTransformation(this, 20, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(mDynamicDetailPhoto);
        } else {
            //没有照片
            mDynamicDetailPhoto.setVisibility(View.GONE);
        }
        mDynamicDetailTime.setText(mDynamic.getCreatedAt());
        mDynamicDetailLocation.setText(mLocationDistance+" m");
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
}
