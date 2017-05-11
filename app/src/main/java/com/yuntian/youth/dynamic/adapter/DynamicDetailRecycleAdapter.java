package com.yuntian.youth.dynamic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;

/**
 * Created by huxianguang on 2017/5/10.
 */

public class DynamicDetailRecycleAdapter extends BaseRecycleViewAdapter {

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_NORMAL = 1;  //说明是不带有header和footer的

    private View mHeaderView;

    public void setHeaderView(View headerView){
        mHeaderView=headerView;
        notifyItemInserted(0);
    }
    public View getHeaderView(){
        return mHeaderView;
    }
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView==null) return TYPE_NORMAL;
        if (position==0) return TYPE_HEADER;//如果没有返回 那么就表示有header  position=0就返回
        return TYPE_NORMAL;
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
