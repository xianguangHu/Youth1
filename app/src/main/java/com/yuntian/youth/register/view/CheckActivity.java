package com.yuntian.youth.register.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.register.presenter.CheckPresenter;
import com.yuntian.youth.register.view.callback.CheckView;
import com.yuntian.youth.widget.SecurityCodeView;
import com.yuntian.youth.widget.SecurityCodeView.InputCompleteListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckActivity extends MvpActivity<CheckView, CheckPresenter> implements InputCompleteListener,CheckView {

    @BindView(R.id.check_setphone)
    TextView mCheckSetphone;
    @BindView(R.id.scv_edittext)
    SecurityCodeView mScvEdittext;
    @BindView(R.id.check_register)
    Button mCheckRegister;
    @BindView(R.id.check_return)
    ImageView mCheckReturn;
    @BindView(R.id.check_erro)
    TextView mCheckErro;

    private String SMSCode;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("phone");
        initView();
        setListener();

    }

    @NonNull
    @Override
    public CheckPresenter createPresenter() {
        return new CheckPresenter();
    }

    private void setListener() {
        mScvEdittext.setInputCompleteListener(this);
    }

    private void initView() {
        mCheckSetphone.setText(mPhone);
    }

    //当验证码为6位时 监听
    @Override
    public void inputComplete() {
        SMSCode = mScvEdittext.getEditContent();
        Log.v("CheckActivity", SMSCode);
    }

    @Override
    public void deleteContent(boolean isDelete) {
    }

    @OnClick({R.id.check_return, R.id.check_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_return://返回
                finish();
                break;
            case R.id.check_register:  //注册
                mCheckErro.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(SMSCode)) {
                    getPresenter().Register(mPhone, SMSCode.trim());
                }else {
                    mCheckErro.setText("验证码不足6位!");
                    mCheckErro.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void CheckErro(String e) {
        mCheckErro.setText(e);
        mCheckErro.setVisibility(View.VISIBLE);
        mScvEdittext.clearEditText();//清除内容

    }

    @Override
    public void Success() {
        //验证成功  填写用户相关信息 登陆
        Intent intent=new Intent(this,RegisterDetailsActivity.class);
        intent.putExtra("phone",mPhone);
        startActivity(intent);
    }
}
