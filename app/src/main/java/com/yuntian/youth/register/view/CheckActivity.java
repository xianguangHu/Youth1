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
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.App;
import com.yuntian.youth.R;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.register.presenter.CheckPresenter;
import com.yuntian.youth.register.view.callback.CheckView;
import com.yuntian.youth.widget.SecurityCodeView;
import com.yuntian.youth.widget.SecurityCodeView.InputCompleteListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckActivity extends MvpActivity<CheckView, CheckPresenter> implements InputCompleteListener, CheckView {

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
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("phone");
        mType = intent.getIntExtra("type", 9);
        initView();
        setListener();

    }

    @NonNull
    @Override
    public CheckPresenter createPresenter() {
        return new CheckPresenter(this);
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
            case R.id.check_register:  //验证
                mCheckErro.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(SMSCode)) {
                    if (mType==Constant.CODE_PASSWORD){
                        Log.v("=====","重置密码");
                        //验证成功  填写密码
                        Intent intent = new Intent(this, RegisterDetailsActivity.class);
                        intent.putExtra("phone", User.getCurrentUser().getMobilePhoneNumber());
                        intent.putExtra("type",Constant.CODE_PASSWORD);
                        intent.putExtra("code",SMSCode);
                        startActivity(intent);
                        return;
                    }
                    getPresenter().Register(mPhone, SMSCode.trim(),mType);
                } else {
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
        if (mType == Constant.CODE_REGISTER) {//说明是注册  通过验证登陆
            //验证成功  填写密码
            Log.v("=====","注册");
            Intent intent = new Intent(this, RegisterDetailsActivity.class);
            intent.putExtra("phone", mPhone);
            intent.putExtra("type",Constant.CODE_REGISTER);
            startActivity(intent);
        } else if (mType == Constant.CODE_REPLACE) {//说明更改手机验证通过
            Log.v("=====","更改手机号");
            Toast.makeText(App.getContext(),"手机号更改成功",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
