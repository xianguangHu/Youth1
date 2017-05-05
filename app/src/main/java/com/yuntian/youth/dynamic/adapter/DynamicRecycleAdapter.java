package com.yuntian.youth.dynamic.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.services.cloud.CloudItem;
import com.bumptech.glide.Glide;
import com.yuntian.youth.R;
import com.yuntian.youth.dynamic.adapter.holder.DynamicHolder;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.DynamicDateil;
import com.yuntian.youth.dynamic.presenter.DynamicPresenter;
import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by huxianguang on 2017/4/20.
 */

public class DynamicRecycleAdapter<T> extends BaseRecycleViewAdapter{
    private Context mContext;
    private DynamicPresenter mPresenter;
    public DynamicRecycleAdapter(Context context, DynamicPresenter presenter){
        mContext=context;
        mPresenter=presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DynamicHolder dynamicHolder= (DynamicHolder) holder;
        final DynamicDateil dynamicDateil= (DynamicDateil) datas.get(position);
        Dynamic dynamic=dynamicDateil.getDynamic();
        CloudItem cloudItem=dynamicDateil.getCloudItem();
        //判断是否匿名 匿名就不显示邮箱和username并且影藏 true表示匿名
        if (!dynamic.isAnonymous()){
            dynamicHolder.mDynamicItemHeadll.setVisibility(View.VISIBLE);
            //设置圆形图片
            Glide.with(mContext).load(Uri.parse(dynamic.getUser().getHeadUri()))
                    .bitmapTransform(new CropCircleTransformation(mContext)).into(dynamicHolder.mDynamicItemHeadIv);
            //用户名
            dynamicHolder.mDynamicItemUsername.setText(dynamic.getUser().getUsername());
        }else {
            //匿名了 隐藏头像和username
            dynamicHolder.mDynamicItemHeadll.setVisibility(View.GONE);
        }
        //显示内容和照片
        dynamicHolder.mDynamicItemContent.setText(dynamic.getContent());
        //判断是否有照片 没有则隐藏


        if (dynamic.getPhotoUri()!=null) {
            dynamicHolder.mDynamicItemPhoto.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Uri.parse(dynamic.getPhotoUri()))
                    .bitmapTransform(new CropTransformation(mContext, 1400, 500, CropTransformation.CropType.CENTER)
                    ,new RoundedCornersTransformation(mContext, 20, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(dynamicHolder.mDynamicItemPhoto);
        }else {
            //没有照片
            dynamicHolder.mDynamicItemPhoto.setVisibility(View.GONE);
        }
        dynamicHolder.mDynamicItemTime.setText(dynamic.getCreatedAt());
        dynamicHolder.mDynamicItemLocation.setText(cloudItem.getDistance()+" m");

        //点赞
        dynamicHolder.mDynamicItemIvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DynamicPresenter().addLike(dynamicDateil);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


}
