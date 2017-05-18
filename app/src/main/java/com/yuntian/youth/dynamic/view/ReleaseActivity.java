package com.yuntian.youth.dynamic.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.yuntian.youth.Utils.DialogUtil;
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

    protected static final int PHOTO_DEKETE = 0;//删除照片
    protected static final int PHOTO_PREVIEW = 1;//预览照片
    private String mPath;
    private String mPhotoUri;

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
        //圆形
        Glide.with(this).load(Uri.parse(User.getCurrentUser().getHeadUri())).bitmapTransform(new CropCircleTransformation(this)).into(mDynamicReleaseIv);
        mDynamicReleaseName.setText(User.getCurrentUser().getUsername());

        mDynamicReleasePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showOperationDialog();
            }
        });
    }
    //显示操作图片dialog
    private void showOperationDialog() {
        String[] item = {"删除照片", "预览大图"};
        String title="操作";
        DialogUtil.showChoosePicDialog(this, item, title, new DialogUtil.showDialogCallBack() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case PHOTO_DEKETE://删除
                        if (mDynamicReleasePhoto.getVisibility()==View.VISIBLE){
                            //回收资源
                            releaseImageViewResouce(mDynamicReleasePhoto);
                            //表示现在图片已经显示 将图片展示隐藏  拍照图标显示
                            mDynamicReleasePhoto.setVisibility(View.GONE);
                            //将拍照的图标隐藏
                            mDynamicReleaseCamera.setVisibility(View.VISIBLE);
                        }
                        //清除glide内存缓存  在主线程中执行
                        mDynamicReleasePhoto.post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.get(ReleaseActivity.this).clearMemory();
                            }
                        });
                        mPhotoUri=null;
                        break;
                    case PHOTO_PREVIEW://预览
                        if (mPhotoUri != null) {
                            Intent intent = new Intent(ReleaseActivity.this, LoadImageActivity.class);
                            intent.putExtra("photoUri", mPhotoUri);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
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

    //显示拍照或者相册中选择dialog
    private void showChoosePicDialog() {
        String[] item = {"选择本地图像", "拍照"};
        String title="上传照片";
        DialogUtil.showChoosePicDialog(this, item, title, new DialogUtil.showDialogCallBack() {
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
    }

    //takephoto
    @Override
    public void takeSuccess(TResult result) {
        //显示照片
        if (mDynamicReleasePhoto.getVisibility()==View.GONE){
            //如果是隐藏状态就显示
            mDynamicReleasePhoto.setVisibility(View.VISIBLE);
            //将拍照的图标隐藏
            mDynamicReleaseCamera.setVisibility(View.GONE);
        }
        Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
        List<TImage> images = result.getImages();
        Log.v("路径", images.get(0).getOriginalPath());
        mPath = images.get(0).getOriginalPath();
        mPhotoUri = "file://"+images.get(0).getOriginalPath();
        Glide.with(this).load(mPhotoUri)
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

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
}
