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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.chat.adapter.ChatAdapter;
import com.yuntian.youth.chat.presenter.ChatPresenter;
import com.yuntian.youth.chat.view.callback.ChatView;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by huxianguang on 2017/5/14.
 */

public class ChatActivity extends MvpActivity<ChatView,ChatPresenter> implements ChatView{
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mUser = (User) getIntent().getSerializableExtra("User");
        initView();

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
        mChatAdapter = new ChatAdapter();
        mChatRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecycleView.setAdapter(mChatAdapter);
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
        String message=mChatEditView.getText().toString();
        if (!TextUtils.isEmpty(message)){
            getPresenter().sendMessage(message,mUser);
        }
    }

    @Override
    public void MessageSuccess(String message) {
        mChatAdapter.addMessage(message);
        mChatAdapter.notifyDataSetChanged();
    }
}
