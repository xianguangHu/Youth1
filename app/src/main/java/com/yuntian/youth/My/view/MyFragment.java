package com.yuntian.youth.My.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.squareup.picasso.Picasso;
import com.yuntian.youth.My.presenter.MyPresenter;
import com.yuntian.youth.My.view.callback.MyView;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.CircularImages;
import com.yuntian.youth.Utils.ImageUtil;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */

public class MyFragment extends MvpFragment<MyView,MyPresenter> implements MyView{
    protected static final int CHOOSE_PICTURE = 0;//相册
    protected static final int TAKE_PICTURE = 1;//拍照
    private static final String path = "file:///sdcard/temp.jpg";

    @BindView(R.id.my_fragment_iv)
    ImageView mMyFragmentIv;
    Unbinder unbinder;


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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        Uri uri = Uri.parse(User.getCurrentUser().getHeadUri());
        if (uri!=null) {
            Picasso.with(getActivity()).load(uri).transform(new CircularImages()).into(mMyFragmentIv);
        }else {
            Picasso.with(getActivity()).load(R.mipmap.sms1).transform(new CircularImages()).into(mMyFragmentIv);
        }
    }

    @OnClick(R.id.my_fragment_iv)
    public void onViewClicked() {
        //修改头像 弹出dialog
        showChoosePicDialog();
    }
    //
    private void showChoosePicDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("修改头像");
        String[] item={"选择本地图像","拍照"};
        builder.setNegativeButton("取消",null);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case CHOOSE_PICTURE://相册
                        Log.v("相册","===");
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
        builder.create().show();
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
        if(mPhoto !=null){
            Bitmap[] bitmaps={mPhoto};
            getPresenter().uploadHead(bitmaps,getActivity());

        }

    }

    @Override
    public void UpdateHeadSuccess(String uri) {
        Picasso.with(getActivity()).load(Uri.parse(uri)).transform(new CircularImages()).into(mMyFragmentIv);
    }

    @Override
    public void Erro(String e) {

    }
}
