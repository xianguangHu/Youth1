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
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yuntian.youth.R;
import com.yuntian.youth.chat.adapter.MessageAdapter;
import com.yuntian.youth.chat.listener.EMMessageListenerAdapter;
import com.yuntian.youth.chat.model.ConversationItem;
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
        mMessageAdapter = new MessageAdapter(getActivity());
        mFragmentMessageRecycleView.setAdapter(mMessageAdapter);
        //来消息时监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
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

    /**
     * 跟新完会话
     * @param conversationItem
     */
    @Override
    public void update2Conversations(ConversationItem conversationItem) {
        mMessageAdapter.getDatas().add(conversationItem);
        mMessageAdapter.notifyDataSetChanged();
    }

    EMMessageListenerAdapter msgListener=new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            getPresenter().loadAllConversations();
            Log.v("收到信息","=========");
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不需要的时候移除listener
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
