package com.yuntian.youth.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntian.youth.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huxianguang on 2017/5/15.
 */

public class BaseChatHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.chat_item_userIv)
    public ImageView mChatItemUserIv;
    @BindView(R.id.chat_item_messgae)
    public TextView mChatItemMessgae;

    public BaseChatHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
