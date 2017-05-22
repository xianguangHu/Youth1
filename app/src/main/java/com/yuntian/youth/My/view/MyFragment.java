package com.yuntian.youth.My.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.yuntian.youth.My.presenter.MyPresenter;
import com.yuntian.youth.My.view.callback.MyView;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.DialogUtil;
import com.yuntian.youth.Utils.ImageUtil;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */

public class MyFragment extends MvpFragment<MyView, MyPresenter> implements MyView, AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    @BindView(R.id.my_fragment_username)
    TextView mMyFragmentUsername;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    protected static final int CHOOSE_PICTURE = 0;//相册
    protected static final int TAKE_PICTURE = 1;//拍照
    private static final String path = "file:///sdcard/temp.jpg";
    @BindView(R.id.my_fragment_linearlayout_detail)
    AutoLinearLayout mMyFragmentLinearlayoutDetail;
    @BindView(R.id.my_fragment_appbar)
    AppBarLayout mMyFragmentAppbar;
    @BindView(R.id.my_fragment_textview_title)
    TextView mMyFragmentTextviewTitle;

    Unbinder unbinder;
    @BindView(R.id.my_fragment_iv)
    ImageView mMyFragmentIv;
    @BindView(R.id.my_fragment_toolbar)
    Toolbar mMyFragmentToolbar;


    private Uri mMTempUri;
    private Bitmap mPhoto;

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public MyPresenter createPresenter() {
        return new MyPresenter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mMyFragmentAppbar.addOnOffsetChangedListener(this);
        mMyFragmentToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mMyFragmentTextviewTitle, 0, View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        Uri uri = Uri.parse(User.getCurrentUser().getHeadUri());
        if (uri != null) {
            Glide.with(getActivity()).load(uri).bitmapTransform(new CropCircleTransformation(getActivity())).into(mMyFragmentIv);
        } else {
            Glide.with(getActivity()).load(R.mipmap.sms1).bitmapTransform(new CropCircleTransformation(getActivity())).into(mMyFragmentIv);
        }
        String username=User.getCurrentUser().getUsername();
        mMyFragmentTextviewTitle.setText(username);
        mMyFragmentUsername.setText(username);

    }

    @OnClick(R.id.my_fragment_iv)
    public void onViewClicked() {
        //修改头像 弹出dialog
        showChoosePicDialog();
    }

    //
    private void showChoosePicDialog() {
        String[] item = {"选择本地图像", "拍照"};
        String title = "修改头像";
        DialogUtil.showChoosePicDialog(getActivity(), item, title, new DialogUtil.showDialogCallBack() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE://相册
                        Log.v("相册", "===");
                        Intent local = new Intent(Intent.ACTION_GET_CONTENT);
                        local.setType("image/*");
                        startActivityForResult(local, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        mMTempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMTempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("返回了", requestCode + "");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    Intent intent1 = ImageUtil.PhotoCutUtil(mMTempUri);
                    startActivityForResult(intent1, Constant.CROP_SMALL_PICTURE);
                    break;
                case CHOOSE_PICTURE://相册
                    Intent intent = ImageUtil.PhotoCutUtil(data.getData());
                    startActivityForResult(intent, Constant.CROP_SMALL_PICTURE);
                    break;
                case Constant.CROP_SMALL_PICTURE: //得到裁剪的照片
                    if (data != null) {
                        //将照片显示到界面
                        setImage2View(data);

                    }
                    break;
            }
        }
    }

    void setImage2View(Intent data) {
        Bundle extras = data.getExtras();
        mPhoto = extras.getParcelable("data");
        if (mPhoto != null) {
            Bitmap[] bitmaps = {mPhoto};
            getPresenter().uploadHead(bitmaps, getActivity());

        }

    }

    @Override
    public void UpdateHeadSuccess(String uri) {
        Glide.with(getActivity()).load(Uri.parse(uri)).bitmapTransform(new CropCircleTransformation(getActivity())).into(mMyFragmentIv);
    }

    @Override
    public void Erro(String e) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static void startAlphaAnimation(View view, long duration, int visibillity) {
        AlphaAnimation alphaAnimation = (visibillity == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = Math.abs(verticalOffset) / maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);


    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mMyFragmentTextviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mMyFragmentTextviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mMyFragmentLinearlayoutDetail, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mMyFragmentLinearlayoutDetail, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }

    }
}
