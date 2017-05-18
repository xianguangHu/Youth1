package com.yuntian.youth.dynamic.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.presenter.LoadImagePresenter;
import com.yuntian.youth.dynamic.view.callback.LoadImageView;
import com.yuntian.youth.widget.ZoomImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huxianguang on 2017/5/17.
 */

public class LoadImageActivity extends MvpActivity<LoadImageView,LoadImagePresenter> implements LoadImageView{
    @BindView(R.id.load_image_iv)
    ZoomImageView mLoadImageIv;
    @BindView(R.id.load_image_finsh)
    ImageView mLoadImageFinsh;
    @BindView(R.id.load_image_save)
    Button mLoadImageSave;
    private String mPhotoUri;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        ButterKnife.bind(this);
        mPhotoUri = getIntent().getStringExtra("photoUri");
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.black));//状态栏颜色
        init();
    }

    @NonNull
    @Override
    public LoadImagePresenter createPresenter() {
        return new LoadImagePresenter(this);
    }

    private void init() {
        Glide.with(this).load(mPhotoUri).asBitmap().into(mLoadImageIv);
    }

    @OnClick({R.id.load_image_finsh,R.id.load_image_save})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.load_image_finsh://关闭
                finish();
                break;
            case R.id.load_image_save://保存图片到本地相册
                getPresenter().savePhotoToSystem(mPhotoUri);
                break;
        }
    }


    //保存成功
    @Override
    public void onDownLoadSuccess() {
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
    }
    //保存失败
    @Override
    public void onDownLoadErro() {
        Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();

    }
}
