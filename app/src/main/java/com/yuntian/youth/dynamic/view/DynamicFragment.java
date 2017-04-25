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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.LoctionUtils;
import com.yuntian.youth.dynamic.adapter.DynamicRecycleAdapter;

import java.util.ArrayList;
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
public class DynamicFragment extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        unbinder = ButterKnife.bind(this, view);
//        mEditTextBodyLl.bringToFront();
        mDynamicRelatedPtr.disableWhenHorizontalMove(true);
        initEvent();
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        mRecyclerAdapter = new DynamicRecycleAdapter();
        mDynamicRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Integer> datas = new ArrayList();
        for (int i = 0; i < 100; i++) {
            datas.add(i);
        }
        mRecyclerAdapter.setDatas(datas);
        mDynamicRecycleView.setAdapter(mRecyclerAdapter);
    }

    private void initEvent() {
        mDynamicRelatedPtr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
        mDynamicRelatedPtr.autoRefresh();
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

}
