package com.yuntian.youth.mian.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.yuntian.youth.R;

import java.util.List;

/**
 * Created by huxianguang on 2017/4/19.
 */

public class FragmentsAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private Context mContext;
    private int[] imageId={
            R.mipmap.location_,R.mipmap.my
    };
    public FragmentsAdapter(FragmentManager fm, List<Fragment> fragmentsList, Context context) {
        super(fm);
        mFragmentList=fragmentsList;
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable image = mContext.getResources().getDrawable(imageId[position]);
        image.setBounds(0, 0, 100, 100);
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
