package com.yuntian.youth.mian.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yuntian.youth.My.view.MyFragment;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.dynamic.view.DynamicFragment;
import com.yuntian.youth.mian.adapter.FragmentsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.main_viewPager)
    ViewPager mMainViewPager;
    @BindView(R.id.main_tablayout)
    TabLayout mMainTablayout;
    private static List<Fragment> fragmentList = new ArrayList<>();
    private FragmentsAdapter mFragmentPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(false,this,getColor(R.color.green_main));
//        StatusBarCompat.translucentStatusBar(this,true);
        initFragment();
    }


    private void initFragment() {
        fragmentList.add(new DynamicFragment());
        fragmentList.add(new MyFragment());
        setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        mFragmentPagerAdapter = new FragmentsAdapter(getSupportFragmentManager(),fragmentList,this);
        mMainViewPager.setAdapter(mFragmentPagerAdapter);
        mMainTablayout.setupWithViewPager(mMainViewPager);
    }

}
