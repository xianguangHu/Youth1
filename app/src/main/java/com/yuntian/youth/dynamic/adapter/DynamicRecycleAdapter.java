package com.yuntian.youth.dynamic.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuntian.youth.R;
import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;

/**
 * Created by huxianguang on 2017/4/20.
 */

public class DynamicRecycleAdapter<T> extends BaseRecycleViewAdapter{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHoder extends ViewHolder{

        public ViewHoder(View itemView) {
            super(itemView);
        }

    }
}
