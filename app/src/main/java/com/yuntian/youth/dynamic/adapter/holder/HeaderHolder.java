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
 * Created by huxianguang on 2017/5/11.
 */

public class HeaderHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.dynamic_detail_head)
    public ImageView mDynamicDetailHead;
    @BindView(R.id.dynamic_detail_username)
    public TextView mDynamicDetailUsername;
    @BindView(R.id.dynamic_detail_headll)
    public AutoLinearLayout mDynamicDetailHeadll;
    @BindView(R.id.dynamic_detail_content)
    public TextView mDynamicDetailContent;
    @BindView(R.id.dynamic_detail_iv_on)
    public ImageView mDynamicDetailIvOn;
    @BindView(R.id.dynamic_detail_likenumber)
    public TextView mDynamicDetailLikenumber;
    @BindView(R.id.dynamic_detail_iv_under)
    public ImageView mDynamicDetailIvUnder;
    @BindView(R.id.dynamic_detail_likell)
    public AutoLinearLayout mDynamicDetailLikell;
    @BindView(R.id.dynamic_detail_photo)
    public ImageView mDynamicDetailPhoto;
    @BindView(R.id.dynamic_detail_time)
    public TextView mDynamicDetailTime;
    @BindView(R.id.dynamic_detail_location)
    public TextView mDynamicDetailLocation;

    public HeaderHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
