package com.yuntian.youth.dynamic.view;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.KeyBoardUtils;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.presenter.DynamicDetailPresenter;
import com.yuntian.youth.dynamic.view.callback.DynamicDetailView;
import com.yuntian.youth.widget.TitleBar;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by huxianguang on 2017/5/8.
 *
 */

public class DynamicDetailActivity extends MvpActivity<DynamicDetailView,DynamicDetailPresenter> implements DynamicDetailView{
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
    @BindView(R.id.dynamic_detail_recyclerView)
    RecyclerView mDynamicDetailRecyclerView;
    @BindView(R.id.dynamic_detail_ptr)
    PtrClassicFrameLayout mDynamicDetailPtr;
    @BindView(R.id.comment_edittext)
    EditText mCommentEdittext;
    @BindView(R.id.comment_iv)
    ImageView mCommentIv;

    private Dynamic mDynamic;
    private String mLocationDistance;
    private boolean mIsOpenBoard;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        ButterKnife.bind(this);
        mIsOpenBoard = getIntent().getBooleanExtra("isOpenBoard",false);
        mDynamic = (Dynamic) getIntent().getSerializableExtra("detail");
        mLocationDistance = getIntent().getStringExtra("locationDistance");
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
        initView();
    }

    @NonNull
    @Override
    public DynamicDetailPresenter createPresenter() {
        return new DynamicDetailPresenter();
    }

    private void initView() {
        initTitle();
        initEvent();
        initRecyclerView();
        //判断是否要打开键盘
        if (mIsOpenBoard){
            //打开键盘
            mCommentEdittext.requestFocus();
            KeyBoardUtils.openKeybord(mCommentEdittext,this);
        }

        //点击屏幕 收起键盘
        mDynamicDetailRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardUtils.closeKeybord(mCommentEdittext,DynamicDetailActivity.this);
                return true;
            }
        });
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
        mDynamicDetailLocation.setText(mLocationDistance + " m");
    }

    //刷新动作
    private void initEvent() {
        mDynamicDetailPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {//底部加载
                Log.v("=====", "上拉");
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {//下拉加载
                Log.v("=====", "下拉");
            }
        });

    }

    private void initRecyclerView() {
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

    @OnClick(R.id.comment_iv)
    public void onViewClicked() {
        //评论
        String content=mCommentEdittext.getText().toString();
        if (!TextUtils.isEmpty(content)) getPresenter().sendComment(content,mDynamic,null,this);
    }

    @Override
    public void commentSuccess() {
        //清空exittext  隐藏键盘
        KeyBoardUtils.closeKeybord(mCommentEdittext,this);
        mCommentEdittext.setText("");

    }
}
