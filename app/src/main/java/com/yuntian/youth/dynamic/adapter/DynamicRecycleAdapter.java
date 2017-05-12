package com.yuntian.youth.dynamic.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.cloud.CloudItem;
import com.bumptech.glide.Glide;
import com.yuntian.youth.R;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.DynamicDateil;
import com.yuntian.youth.dynamic.view.DynamicDetailActivity;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;
import com.yuntian.youth.register.view.callback.DynamicCallBack;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by huxianguang on 2017/4/20.
 */

public class DynamicRecycleAdapter<T> extends BaseRecycleViewAdapter{
    private Context mContext;
    private DynamicCallBack mDynamicCallBack;
    private OnItemClickListener onItemClickListener;

    public DynamicRecycleAdapter(Context context, DynamicCallBack callBack){
        mContext=context;
        mDynamicCallBack=callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.v("======","success");
        DynamicHolder dynamicHolder= (DynamicHolder) holder;
        ((DynamicHolder) holder).setPosition(position);
        final DynamicDateil dynamicDateil= (DynamicDateil) datas.get(position);
        final Dynamic dynamic=dynamicDateil.getDynamic();
        final CloudItem cloudItem=dynamicDateil.getCloudItem();
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

        //显示点赞数
        dynamicHolder.mDynamicItemLikenumber.setText(dynamic.getLikes()+"");
        //点赞
        dynamicHolder.mDynamicItemIvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先去判断是否已经点过赞 点过赞就不在执行
                if (!dynamicDateil.isLike()) {
                    dynamicDateil.setLike(true);
                    //是否点过睬
                    if (dynamicDateil.isUnLike()){
                        dynamicDateil.setUnLike(false);
                        //表示点过睬 在点赞就要加2
                        mDynamicCallBack.addLike(Constant.LIKE_TYPE_TWO,dynamicDateil,position);
                    }else {
                        mDynamicCallBack.addLike(Constant.LIKE_TYPE_ONE,dynamicDateil,position);
                    }
                }
            }
        });
        //睬
        dynamicHolder.mDynamicItemivUnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否点过睬  点过就表示不在执行
                if (!dynamicDateil.isUnLike()){
                    dynamicDateil.setUnLike(true);
                    //判断是否点过赞  点过zan就减2
                    if (dynamicDateil.isLike()){
                        dynamicDateil.setLike(false);
                        mDynamicCallBack.addLike(Constant.UNLIKE_TYPE_TWO,dynamicDateil,position);
                    }else {
                        //减一
                        mDynamicCallBack.addLike(Constant.UNLIKE_TYPE_ONE,dynamicDateil,position);
                    }
                }
            }
        });

        dynamicHolder.mDynamicItemComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, DynamicDetailActivity.class);
                //true 直接打开键盘
                intent.putExtra("isOpenBoard",true);
                Bundle bundle=new Bundle();
                bundle.putSerializable("detail",dynamic);
                intent.putExtras(bundle);
                intent.putExtra("locationDistance",cloudItem.getDistance()+"");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public static interface OnItemClickListener {
        public void onItemClick(View view, boolean isLongClick,int postion);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class DynamicHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        @BindView(R.id.dynamic_item_headIv)
        public ImageView mDynamicItemHeadIv;
        @BindView(R.id.dynamic_item_username)
        public TextView mDynamicItemUsername;
        @BindView(R.id.dynamic_item_content)
        public TextView mDynamicItemContent;
        @BindView(R.id.dynamic_item_photo)
        public ImageView mDynamicItemPhoto;
        @BindView(R.id.dynamic_item_iv_on)
        public ImageView mDynamicItemIvOn;
        @BindView(R.id.dynamic_item_likenumber)
        public TextView mDynamicItemLikenumber;
        @BindView(R.id.dynamic_item_iv_under)
        public ImageView mDynamicItemivUnder;
        @BindView(R.id.dynamic_item_headll)
        public AutoLinearLayout mDynamicItemHeadll;
        @BindView(R.id.dynamic_item_time)
        public TextView mDynamicItemTime;
        @BindView(R.id.dynamic_item_location)
        public TextView mDynamicItemLocation;
        @BindView(R.id.dynamic_item_comment)
        public ImageView mDynamicItemComment;
        int mposition;

        public DynamicHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handleClick(v,false);
        }

        @Override
        public boolean onLongClick(View v) {
            return handleClick(v, true);
        }

        public void setPosition(int position){
            mposition=position;
        }
        public boolean handleClick(View view,boolean isLongClick){
            if (onItemClickListener!=null){
                onItemClickListener.onItemClick(view,isLongClick,mposition);
                return true;
            }
            return false;
        }

    }
}
