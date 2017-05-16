package com.yuntian.youth.chat.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yuntian.youth.R;
import com.yuntian.youth.chat.model.ChatItem;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatAdapter extends BaseRecycleViewAdapter{
    private Context mContext;

    public ChatAdapter(Context context){
        mContext=context;
    }

    public void addChatItem(ChatItem chatItem){
        datas.add(chatItem);
    }

    @Override
    public int getItemViewType(int position) {
        ChatItem chatItem= (ChatItem) datas.get(position);
        return chatItem.getType();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResource = 0;
        switch (viewType){
            case Constant.CHAT_MESSAGE_TYPE_MY://自己发送的消息
                layoutResource=R.layout.chat_item_my;
                break;
            case Constant.CHAT_MESSAGE_TYPE_FRIENDS://好友发送的消息
                layoutResource=R.layout.chat_item_friends;
                break;
        }
        View view= LayoutInflater.from(mContext).inflate(layoutResource,parent,false);
        return new com.yuntian.youth.chat.adapter.BaseChatHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        com.yuntian.youth.chat.adapter.BaseChatHolder chatHolder= (com.yuntian.youth.chat.adapter.BaseChatHolder) holder;
        ChatItem chatItem= (ChatItem) datas.get(position);

        Glide.with(mContext).load(Uri.parse(chatItem.getUser().getHeadUri()))
                .bitmapTransform(new CropCircleTransformation(mContext)).into(chatHolder.mChatItemUserIv);

        chatHolder.mChatItemMessgae.setText(chatItem.getMessageContent());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
