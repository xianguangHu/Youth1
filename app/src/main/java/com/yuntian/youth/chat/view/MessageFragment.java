package com.yuntian.youth.chat.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hyphenate.chat.EMConversation;
import com.yuntian.youth.R;
import com.yuntian.youth.chat.adapter.MessageAdapter;
import com.yuntian.youth.chat.presenter.MessagePresenter;
import com.yuntian.youth.chat.view.callback.MessageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by huxianguang on 2017/5/15.
 */

public class MessageFragment extends MvpFragment<MessageView, MessagePresenter> implements MessageView{

    @BindView(R.id.fragment_message_recycleView)
    RecyclerView mFragmentMessageRecycleView;
    Unbinder unbinder;
    private MessageAdapter mMessageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mFragmentMessageRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessageAdapter = new MessageAdapter();
        mFragmentMessageRecycleView.setAdapter(mMessageAdapter);
    }

    @Override
    public MessagePresenter createPresenter() {
        return new MessagePresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void update2Conversations(List<EMConversation> emConversations) {
        mMessageAdapter.setDatas(emConversations);
        mMessageAdapter.notifyDataSetChanged();
        Log.v("长度====",emConversations.get(0).getLastMessage().getBody().toString());
    }
}
