package com.yuntian.youth.dynamic.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.KeyBoardUtils;
import com.yuntian.youth.dynamic.adapter.DynamicDetailRecycleAdapter;
import com.yuntian.youth.dynamic.model.Comment;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.presenter.DynamicDetailPresenter;
import com.yuntian.youth.dynamic.view.callback.DynamicDetailView;
import com.yuntian.youth.widget.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by huxianguang on 2017/5/12.
 */

public class DetailListFragment extends MvpFragment<DynamicDetailView, DynamicDetailPresenter> implements DynamicDetailView {
    @BindView(R.id.dynamic_detail_recyclerView)
    RecyclerView mDynamicDetailRecyclerView;
    @BindView(R.id.dynamic_detail_ptr)
    PtrClassicFrameLayout mDynamicDetailPtr;
    Unbinder unbinder;
    @BindView(R.id.comment_edittext)
    EditText mCommentEdittext;
    @BindView(R.id.comment_iv)
    ImageView mCommentIv;
    private DynamicDetailRecycleAdapter mDynamicDetailRecycleAdapter;
    private Dynamic mDynamic;
    private String mLocationDistance;
    private boolean mIsOpenBoard;//判断是否要打开键盘

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_list, container, false);

        unbinder = ButterKnife.bind(this, view);
        initView();
        initRecyclerView();
        initEvent();
        return view;
    }

    private void initView() {
        //判断是否要打开键盘
        if (mIsOpenBoard) {
            //打开键盘
            mCommentEdittext.requestFocus();
            KeyBoardUtils.openKeybord(mCommentEdittext,getActivity());
        }

        //点击屏幕 收起键盘
        mDynamicDetailRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    //关闭键盘
                    KeyBoardUtils.closeKeybord(mCommentEdittext,getActivity());
                return false;
            }
        });

    }

    @Override
    public DynamicDetailPresenter createPresenter() {
        return new DynamicDetailPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //刷新动作
    private void initEvent() {
        mDynamicDetailPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {//底部加载
                Log.v("=====", "上拉");

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {//下拉加载
                Log.v("=====", "下拉");
                getPresenter().getData(mDynamic, getActivity());

            }
        });

    }

    private void initRecyclerView() {
        mDynamicDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDynamicDetailRecycleAdapter = new DynamicDetailRecycleAdapter(getActivity(), mDynamic, mLocationDistance);
        mDynamicDetailRecyclerView.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL, 25, getResources().getColor(R.color.comment_Divider)));
        mDynamicDetailRecyclerView.setAdapter(mDynamicDetailRecycleAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDynamic = ((DynamicDetailActivity) context).getDynamicData();
        mLocationDistance = ((DynamicDetailActivity) context).getLocationDistance();
        mIsOpenBoard = ((DynamicDetailActivity) context).getIsOpenBoard();
    }

    @Override
    public void commentSuccess() {
            //清空exittext  隐藏键盘
            KeyBoardUtils.closeKeybord(mCommentEdittext, getActivity());
            mCommentEdittext.setText("");
    }

    @Override
    public void UpdateData2Comment(List<Comment> datas) {
        //关闭刷新
        mDynamicDetailPtr.refreshComplete();
        if (datas != null) {
            mDynamicDetailRecycleAdapter.setDatas(datas);
        }
        mDynamicDetailRecycleAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.comment_iv)
    public void onViewClicked() {
        //评论
        String content = mCommentEdittext.getText().toString();
        if (!TextUtils.isEmpty(content)) getPresenter().sendComment(content, mDynamic, null, getActivity());
    }
}
