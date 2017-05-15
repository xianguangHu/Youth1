package com.yuntian.youth.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;

/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatAdapter extends BaseRecycleViewAdapter{

    public void addMessage(String message){
        datas.add(message);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
