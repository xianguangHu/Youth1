package com.yuntian.youth.dynamic.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.KeyBoardUtils;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.adapter.DynamicDetailRecycleAdapter;
import com.yuntian.youth.dynamic.model.Comment;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.presenter.DynamicDetailPresenter;
import com.yuntian.youth.dynamic.view.callback.DynamicDetailView;
import com.yuntian.youth.widget.RecycleViewDivider;
import com.yuntian.youth.widget.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by huxianguang on 2017/5/8.
 */

public class DynamicDetailActivity extends MvpActivity<DynamicDetailView, DynamicDetailPresenter> implements DynamicDetailView {


    @BindView(R.id.dynamic_detail_title)
    TitleBar mDynamicDetailTitle;
    @BindView(R.id.dynamic_detail_recyclerView)
    RecyclerView mDynamicDetailRecyclerView;
    @BindView(R.id.dynamic_detail_ptr)
    PtrClassicFrameLayout mDynamicDetailPtr;
    @BindView(R.id.comment_edittext)
    EditText mCommentEdittext;
    @BindView(R.id.comment_iv)
    ImageView mCommentIv;
    private Dynamic mDynamic;
    private String mLocationDistance;//距离多少米
    private boolean mIsOpenBoard;//是否打开键盘
    private DynamicDetailRecycleAdapter mDynamicDetailRecycleAdapter;

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
        if (mIsOpenBoard) {
            //打开键盘
            mCommentEdittext.requestFocus();
            KeyBoardUtils.openKeybord(mCommentEdittext, this);
        }

        //点击屏幕 收起键盘
        mDynamicDetailRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardUtils.closeKeybord(mCommentEdittext, DynamicDetailActivity.this);
                return true;
            }
        });


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
                getPresenter().getData(mDynamic,DynamicDetailActivity.this);

            }
        });

    }

    private void initRecyclerView() {
        mDynamicDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDynamicDetailRecycleAdapter = new DynamicDetailRecycleAdapter(this,mDynamic,mLocationDistance);
        mDynamicDetailRecyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 25, getResources().getColor(R.color.comment_Divider)));
        mDynamicDetailRecyclerView.setAdapter(mDynamicDetailRecycleAdapter);
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
        String content = mCommentEdittext.getText().toString();
        if (!TextUtils.isEmpty(content)) getPresenter().sendComment(content, mDynamic, null, this);
    }

    @Override
    public void commentSuccess() {
        //清空exittext  隐藏键盘
        KeyBoardUtils.closeKeybord(mCommentEdittext, this);
        mCommentEdittext.setText("");
    }

    //获取数据成功
    @Override
    public void UpdateData2Comment(List<Comment> datas) {
        //关闭刷新
        mDynamicDetailPtr.refreshComplete();
        if (datas!=null){
            mDynamicDetailRecycleAdapter.setDatas(datas);
        }
        mDynamicDetailRecycleAdapter.notifyDataSetChanged();
    }
}
