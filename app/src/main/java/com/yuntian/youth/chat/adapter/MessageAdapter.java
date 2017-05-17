package com.yuntian.youth.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.yuntian.youth.R;
import com.yuntian.youth.chat.model.ConversationItem;
import com.yuntian.youth.chat.view.ChatActivity;
import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by huxianguang on 2017/5/15.
 * 会话列表的adapter
 */

public class MessageAdapter extends BaseRecycleViewAdapter {
    private Context mContext;

    public MessageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.conversations_item, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageHolder messageHolder = (MessageHolder) holder;
        final ConversationItem conversationItem = (ConversationItem) datas.get(position);
        EMMessage emMessage = conversationItem.geteMConversation().getLastMessage();
        messageHolder.mConversationsItemLastMesage.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
        messageHolder.mConversationsItemUsername.setText(conversationItem.getUser().getUsername());
        Glide.with(mContext).load(Uri.parse(conversationItem.getUser().getHeadUri()))
                .bitmapTransform(new CropCircleTransformation(mContext)).into(messageHolder.mConversationsItemUserIv);
        messageHolder.mConversationsItemTime.setText(emMessage.getMsgTime() + "");
        //设置监听
        messageHolder.mConversationsItemLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("User",conversationItem.getUser());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.conversations_item_userIv)
        public ImageView mConversationsItemUserIv;
        @BindView(R.id.conversations_item_username)
        public TextView mConversationsItemUsername;
        @BindView(R.id.conversations_item_time)
        public TextView mConversationsItemTime;
        @BindView(R.id.conversations_item_lastMesage)
        public TextView mConversationsItemLastMesage;
        @BindView(R.id.conversations_item_ll)
        AutoLinearLayout mConversationsItemLl;

        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
