package com.yuntian.youth.dynamic.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.LoctionUtils;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.presenter.ReleasePresenter;
import com.yuntian.youth.dynamic.view.callback.ReleaseView;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.TitleBar;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by huxianguang on 2017/4/24.
 * 发布动态页面
 */

public class ReleaseActivity extends MvpActivity<ReleaseView, ReleasePresenter> implements CompoundButton.OnCheckedChangeListener,
        Animation.AnimationListener, ReleaseView, TakePhoto.TakeResultListener, InvokeListener {
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
    @BindView(R.id.dynamic_release_photo)
    ImageView mDynamicReleasePhoto;
    @BindView(R.id.dynamic_release_buttom)
    AutoLinearLayout mDynamicReleaseButtom;
    private AlphaAnimation mDissAnimation;
    private String mCoordinates;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private boolean isAnonymous=false;

    private static final String TAG = TakePhotoActivity.class.getName();

    protected static final int CHOOSE_PICTURE = 0;//相册
    protected static final int TAKE_PICTURE = 1;//拍照
    private String mPath;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        initView();
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public ReleasePresenter createPresenter() {
        return new ReleasePresenter();
    }

    private void initView() {
        LoctionUtils.getLocation(new LoctionUtils.MyLocationListener() {
            @Override
            public void result(AMapLocation location) {
                mCoordinates=location.getLongitude()+","+location.getLatitude();
            }
        });
        mDynamicReleaseSwitch.setOnCheckedChangeListener(this);
        initTitle();
        Glide.with(this).load(Uri.parse(User.getCurrentUser().getHeadUri())).bitmapTransform(new CropCircleTransformation(this)).into(mDynamicReleaseIv);
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
                    getPresenter().send(content, mCoordinates, ReleaseActivity.this,mPath,isAnonymous);
                }
            }
        });
    }

    //switch监听事件
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isAnonymous=isChecked;
        if (isChecked) {
            //隐藏
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


//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                //可在其中解析amapLocation获取相应内容。坐标  经度,纬度
//                mCoordinates = aMapLocation.getLongitude() + "," + aMapLocation.getLatitude();
//                Log.v("金纬度", mCoordinates);
//            } else {
//                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError=========", "location Error, ErrCode:"
//                        + aMapLocation.getErrorCode() + ", errInfo:"
//                        + aMapLocation.getErrorInfo());
//            }
//        }
//    }

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
        Toast.makeText(this, e, Toast.LENGTH_SHORT).show();
    }

    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        String[] item = {"选择本地图像", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE://相册
                        takePhoto.onEnableCompress(null, false);
                        takePhoto.onPickFromGallery();
                        break;
                    case TAKE_PICTURE://拍照
                        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                        Uri imageUri = Uri.fromFile(file);
                        takePhoto.onPickFromCapture(imageUri);
                        break;
                }
            }
        });
        builder.create().show();
    }

    //takephoto
    @Override
    public void takeSuccess(TResult result) {
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
        List<TImage> images = result.getImages();
        Log.v("路径", images.get(0).getOriginalPath());
        mPath = images.get(0).getOriginalPath();
        Glide.with(this).load("file://"+images.get(0).getOriginalPath())
                .bitmapTransform(new CropTransformation(this,1300,500, CropTransformation.CropType.CENTER),new RoundedCornersTransformation(this, 20, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(mDynamicReleasePhoto);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

}
