package com.yuntian.youth.dynamic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntian.youth.R;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huxianguang on 2017/5/4.
 */

public class DynamicHolder extends RecyclerView.ViewHolder {
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

    public DynamicHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
