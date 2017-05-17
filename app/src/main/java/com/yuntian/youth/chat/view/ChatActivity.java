package com.yuntian.youth.chat.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.chat.adapter.ChatAdapter;
import com.yuntian.youth.chat.listener.EMMessageListenerAdapter;
import com.yuntian.youth.chat.model.ChatItem;
import com.yuntian.youth.chat.presenter.ChatPresenter;
import com.yuntian.youth.chat.view.callback.ChatView;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatActivity extends MvpActivity<ChatView, ChatPresenter> implements ChatView {
    @BindView(R.id.chat_title)
    TitleBar mChatTitle;
    @BindView(R.id.chat_recycleView)
    RecyclerView mChatRecycleView;
    @BindView(R.id.chat_ptr)
    PtrClassicFrameLayout mChatPtr;
    @BindView(R.id.chat_editView)
    EditText mChatEditView;
    @BindView(R.id.chat_send)
    Button mChatSend;
    private User mUser;
    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mUser = (User) getIntent().getSerializableExtra("User");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //查询会话内容
        getPresenter().loadConversationAllMessage(mUser.getObjectId(), mUser);
        //监听消息
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @NonNull
    @Override
    public ChatPresenter createPresenter() {
        return new ChatPresenter();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));//状态栏颜色
        initTitle();
        initRecycleView();
    }

    private void initRecycleView() {
        mChatAdapter = new ChatAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mChatRecycleView.setLayoutManager(mLinearLayoutManager);
        mChatRecycleView.setAdapter(mChatAdapter);
        mChatRecycleView.addOnScrollListener(mOnScrollListener);
    }

    private void initTitle() {
        mChatTitle.setLeftImageResource(R.mipmap.left);
        mChatTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChatTitle.setTitle(mUser.getUsername());
        mChatTitle.setTitleColor(Color.WHITE);
    }

    @OnClick(R.id.chat_send)
    public void onViewClicked() {
        Log.v("点击事件", "========");
        String message = mChatEditView.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            mChatEditView.setText("");
            ChatItem chatItem = new ChatItem();
            chatItem.setType(Constant.CHAT_MESSAGE_TYPE_MY);
            chatItem.setContext(message);
            chatItem.setUser(User.getCurrentUser());
            //第一时间去显示这条消息 如果消息发送失败在去展示
            mChatAdapter.addChatItem(chatItem);
            mChatAdapter.notifyDataSetChanged();
            //发送消息
            getPresenter().sendMessage(message, mUser);
        }
    }

    @Override
    public void MessageSuccess(String message) {
    }

    @Override
    public void MessageReceived(List<ChatItem> chatItems) {
        mChatAdapter.getDatas().addAll(chatItems);
        mChatAdapter.notifyDataSetChanged();
        smoothScrollToBottom();
    }

    @Override
    public void LoadMoreSuccess(List<ChatItem> chatItems) {
        //加载聊天记录成功
        mChatAdapter.getDatas().addAll(0,chatItems);
        mChatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    EMMessageListenerAdapter msgListener = new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            getPresenter().getMessage(messages);
        }
    };

    /**
     * 将recycleview滚动到最底部
     */
    private void smoothScrollToBottom() {
        mChatRecycleView.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0) {
                    EMMessage firstMessage = ((ChatItem) mChatAdapter.getDatas().get(0)).getEMMessage();
                    getPresenter().loadMoreMessages(mUser, firstMessage);
                }
            }
        }
    };
}
