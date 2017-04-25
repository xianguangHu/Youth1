package com.yuntian.youth.dynamic.view;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.squareup.picasso.Picasso;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.CircularImages;
import com.yuntian.youth.Utils.LoctionUtils;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.presenter.ReleasePresenter;
import com.yuntian.youth.dynamic.view.callback.ReleaseView;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.TitleBar;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huxianguang on 2017/4/24.
 * 发布动态页面
 */

public class ReleaseActivity extends MvpActivity<ReleaseView, ReleasePresenter> implements CompoundButton.OnCheckedChangeListener,
        Animation.AnimationListener, AMapLocationListener,ReleaseView {
    @BindView(R.id.dynamic_release_title)
    TitleBar mDynamicReleaseTitle;
    @BindView(R.id.dynamic_release_iv)
    ImageView mDynamicReleaseIv;
    @BindView(R.id.dynamic_release_name)
    TextView mDynamicReleaseName;
    @BindView(R.id.dynamic_release_switch)
    Switch mDynamicReleaseSwitch;
    @BindView(R.id.dynamic_release_userLl)
    AutoLinearLayout mDynamicReleaseUserLl;
    @BindView(R.id.dynamic_release_content)
    EditText mDynamicReleaseContent;
    @BindView(R.id.dynamic_release_camera)
    ImageView mDynamicReleaseCamera;
    private AlphaAnimation mDissAnimation;
    private String mCoordinates;


    protected static final int CHOOSE_PICTURE = 0;//相册
    protected static final int TAKE_PICTURE = 1;//拍照

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        initView();
        new LoctionUtils(this);
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
    }

    @NonNull
    @Override
    public ReleasePresenter createPresenter() {
        return new ReleasePresenter();
    }

    private void initView() {
        mDynamicReleaseSwitch.setOnCheckedChangeListener(this);
        initTitle();
        Picasso.with(this).load(Uri.parse(User.getCurrentUser().getHeadUri())).transform(new CircularImages()).into(mDynamicReleaseIv);
        mDynamicReleaseName.setText(User.getCurrentUser().getUsername());
    }

    private void initTitle() {
        mDynamicReleaseTitle.setLeftImageResource(R.mipmap.left);
        mDynamicReleaseTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDynamicReleaseTitle.addAction(new TitleBar.ImageAction(R.mipmap.send) {
            @Override
            public void performAction(View view) {
                //发送状态
                //内容不为空
                String content = mDynamicReleaseContent.getText().toString();
                if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(mCoordinates)) {
                    getPresenter().send(content, mCoordinates, ReleaseActivity.this);
                }
            }
        });
    }

    //switch监听事件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //隐藏
//            mDynamicReleaseUserLl.setVisibility(View.GONE);
            mDissAnimation = new AlphaAnimation(1, 0);
            mDissAnimation.setDuration(200);
            mDynamicReleaseUserLl.startAnimation(mDissAnimation);
            mDissAnimation.setAnimationListener(this);
        } else {
            //显示
            mDynamicReleaseUserLl.setVisibility(View.VISIBLE);
            AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
            appearAnimation.setDuration(200);
            mDynamicReleaseUserLl.setAnimation(appearAnimation);
            appearAnimation.setAnimationListener(this);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == mDissAnimation) {
            //动画结束时 隐藏
            mDynamicReleaseUserLl.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。坐标  经度,纬度
                mCoordinates = aMapLocation.getLongitude() + "," + aMapLocation.getLatitude();
//                if (!TextUtils.isEmpty(mCoordinates)){
//                    mData = new JsonParser().parse(mCoordinates).getAsJsonObject();
                Log.v("金纬度", mCoordinates);
//                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError=========", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @OnClick(R.id.dynamic_release_camera)
    public void onViewClicked() {
        showChoosePicDialog();
    }

    @Override
    public void sendSuccess() {
        finish();
    }

    @Override
    public void sendErro(String e) {
        Toast.makeText(this,e,Toast.LENGTH_SHORT).show();
    }

    private void showChoosePicDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        String[] item={"选择本地图像","拍照"};
        builder.setNegativeButton("取消",null);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case CHOOSE_PICTURE://相册
//                        Log.v("相册","===");
//                        Intent local = new Intent(Intent.ACTION_GET_CONTENT);
//                        local.setType("image/*");
//                        startActivityForResult(local, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
//                        Intent openCameraIntent = new Intent(
//                                MediaStore.ACTION_IMAGE_CAPTURE);
//                        mMTempUri = Uri.fromFile(new File(Environment
//                                .getExternalStorageDirectory(), "image.jpg"));
//                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMTempUri);
//                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }
}
