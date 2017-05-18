package com.yuntian.youth.dynamic.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.yuntian.youth.R;
import com.yuntian.youth.dynamic.adapter.DynamicRecycleAdapter;
import com.yuntian.youth.dynamic.model.Dynamic;
import com.yuntian.youth.dynamic.model.DynamicDateil;
import com.yuntian.youth.dynamic.presenter.DynamicPresenter;
import com.yuntian.youth.dynamic.view.callback.DynamicView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.view.callback.DynamicCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 * 动态展示
 */
public class DynamicFragment extends MvpFragment<DynamicView,DynamicPresenter> implements DynamicView{


    @BindView(R.id.dynamic_related_ptr)
    PtrClassicFrameLayout mDynamicRelatedPtr;
    Unbinder unbinder;
    @BindView(R.id.dynamic_recyclerView)
    RecyclerView mDynamicRecycleView;
    @BindView(R.id.dynamic_release)
    FloatingActionButton mDynamicRelease;
    //    @BindView(R.id.editTextBodyLl)
//    AutoLinearLayout mEditTextBodyLl;
    private DynamicRecycleAdapter mRecyclerAdapter;

    public DynamicFragment() {
        // Required empty public constructor
    }

    @Override
    public DynamicPresenter createPresenter() {
        return new DynamicPresenter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        unbinder = ButterKnife.bind(this, view);
        mDynamicRelatedPtr.disableWhenHorizontalMove(true);
        initView();
        initEvent();
        initRecyclerView();
        return view;

    }

    private void initView() {

    }

    private void initRecyclerView() {
        mRecyclerAdapter = new DynamicRecycleAdapter(getActivity(), new DynamicCallBack() {
            @Override
            public void addLike(int type,DynamicDateil dynamicDateil, int position) {
                Log.v("DynamicFragment========","updateLike");
                DynamicDateil dynamicDateil1= (DynamicDateil) mRecyclerAdapter.getDatas().get(position);
                Dynamic dynamic=dynamicDateil1.getDynamic();
                switch (type){
                    case Constant.LIKE_TYPE_ONE://+1
                        dynamic.setLikes(dynamic.getLikes()+1);
                        break;
                    case Constant.LIKE_TYPE_TWO://+2
                        dynamic.setLikes(dynamic.getLikes()+2);
                        break;
                    case Constant.UNLIKE_TYPE_ONE://-1
                        dynamic.setLikes(dynamic.getLikes()-1);
                        break;
                    case Constant.UNLIKE_TYPE_TWO://-2
                        dynamic.setLikes(dynamic.getLikes()-2);
                        break;
                }
                mRecyclerAdapter.notifyDataSetChanged();
                getPresenter().addLike(type,dynamicDateil,position);
            }
        });
        mRecyclerAdapter.setOnItemClickListener(new DynamicRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, boolean isLongClick,int postion) {
                if (isLongClick) {
                    Log.v("======","长按"+postion);
                } else {
                    Log.v("======","点击"+postion);
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),DynamicDetailActivity.class);
                    Bundle bundle=new Bundle();
                    DynamicDateil dynamicDateil= (DynamicDateil) mRecyclerAdapter.getDatas().get(postion);
                    bundle.putSerializable("detail",dynamicDateil.getDynamic());
                    intent.putExtras(bundle);
                    Log.v("=",dynamicDateil.getCloudItem().getDistance()+"");
                    intent.putExtra("locationDistance",dynamicDateil.getCloudItem().getDistance()+"");
                    startActivity(intent);
                }
            }
        });
        mDynamicRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDynamicRecycleView.setAdapter(mRecyclerAdapter);
    }

    private void initEvent() {
        mDynamicRelatedPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {//底部加载
                Log.v("=====","上拉");
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {//下拉加载
                Log.v("=====","下拉");

                getPresenter().getData(getActivity());

            }
        });
//        mDynamicRelatedPtr.autoRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.dynamic_release)
    public void onViewClicked() {
        //跳转到发布页面
        startActivity(new Intent(getActivity(),ReleaseActivity.class));
    }


    //数据跟新完毕
    @Override
    public void update2loadData(List<DynamicDateil> datas) {
        mDynamicRelatedPtr.refreshComplete();
        if (datas!=null){
            mRecyclerAdapter.setDatas(datas);
        }
        mRecyclerAdapter.notifyDataSetChanged();
        //数据显示完毕的时候将数据保存到本地数据库 防止断网的时候不显示数据
        getPresenter().saveDynamicCachedata(datas,getActivity());
    }

    /**
     * 跟新点赞ui
     * @param position
     */
    @Override
    public void updateLike(int position) {

    }
}
