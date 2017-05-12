package com.yuntian.youth.dynamic.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuntian.youth.R;
import com.yuntian.youth.dynamic.adapter.holder.HeaderHolder;
import com.yuntian.youth.dynamic.model.Comment;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.global.adapter.BaseRecycleViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by huxianguang on 2017/5/10.
 */

public class DynamicDetailRecycleAdapter<T> extends BaseRecycleViewAdapter {

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_NORMAL = 1;  //说明是不带有header和footer的

    private Dynamic mDynamic;
    private String mLocationDistance;//距离多少米
    private Context mContext;

    public DynamicDetailRecycleAdapter(Context context,Dynamic dynamic, String locationDistance) {
        mContext = context;
        mDynamic = dynamic;
        mLocationDistance = locationDistance;
    }


    @Override
    public int getItemViewType(int position) {

        if (position==0){
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;

        if (TYPE_HEADER ==viewType) {
            View header = LayoutInflater.from(mContext).inflate(R.layout.comment_heander_item, parent, false);
            viewHolder = new HeaderHolder(header);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            viewHolder = new CommentHolder(view);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            //说明是头 加载header文件
            HeaderHolder headerHolder = (HeaderHolder) holder;
            //判断是否匿名 匿名就不显示邮箱和username并且影藏 true表示匿名
            if (!mDynamic.isAnonymous()) {
                headerHolder.mDynamicDetailHeadll.setVisibility(View.VISIBLE);
                //设置圆形图片
                Glide.with(mContext).load(Uri.parse(mDynamic.getUser().getHeadUri()))
                        .bitmapTransform(new CropCircleTransformation(mContext)).into(headerHolder.mDynamicDetailHead);
                //用户名
                headerHolder.mDynamicDetailUsername.setText(mDynamic.getUser().getUsername());
            } else {
                //匿名了 隐藏头像和username
                headerHolder.mDynamicDetailHeadll.setVisibility(View.GONE);
            }
            headerHolder.mDynamicDetailContent.setText(mDynamic.getContent());
            //显示内容和照片
            headerHolder.mDynamicDetailContent.setText(mDynamic.getContent());
            //判断是否有照片 没有则隐藏
            if (mDynamic.getPhotoUri() != null) {
                headerHolder.mDynamicDetailPhoto.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(Uri.parse(mDynamic.getPhotoUri()))
                        .bitmapTransform(new CropTransformation(mContext, 1400, 500, CropTransformation.CropType.CENTER)
                                , new RoundedCornersTransformation(mContext, 20, 0, RoundedCornersTransformation.CornerType.ALL))
                        .into(headerHolder.mDynamicDetailPhoto);
            } else {
                //没有照片
                headerHolder.mDynamicDetailPhoto.setVisibility(View.GONE);
            }
            headerHolder.mDynamicDetailTime.setText(mDynamic.getCreatedAt());
            headerHolder.mDynamicDetailLocation.setText(mLocationDistance + " m");
            return;
        }

            CommentHolder commentHolder = (CommentHolder) holder;
            //因为有了头文件 所以要减1
            Comment comment = (Comment) datas.get(position-1);
            commentHolder.mCommentItemContent.setText(comment.getContent());
            //圆形
            Glide.with(mContext).load(Uri.parse(comment.getAuthor().getHeadUri())).bitmapTransform(new CropCircleTransformation(mContext)).into(commentHolder.mCommentItemHeaderiv);
            commentHolder.mCommentItemUsername.setText(comment.getAuthor().getUsername());
            commentHolder.mCommentItemTime.setText(comment.getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return datas.size()+1;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_item_headeriv)
        public ImageView mCommentItemHeaderiv;
        @BindView(R.id.comment_item_username)
        public TextView mCommentItemUsername;
        @BindView(R.id.comment_item_content)
        public TextView mCommentItemContent;
        @BindView(R.id.comment_item_time)
        public TextView mCommentItemTime;

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
