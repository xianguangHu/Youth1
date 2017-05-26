package com.yuntian.youth.register.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.App;
import com.yuntian.youth.My.service.MyService;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.StringUtils;
import com.yuntian.youth.global.Constant;
import com.yuntian.youth.mian.view.MainActivity;
import com.yuntian.youth.register.presenter.RegisterDetailsPresenter;
import com.yuntian.youth.register.view.callback.RegisterDetailsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterDetailsActivity extends MvpActivity<RegisterDetailsView, RegisterDetailsPresenter> implements RegisterDetailsView {

    @BindView(R.id.register_details_iv)
    ImageView mRegisterDetailsIv;
    @BindView(R.id.register_details_phone)
    TextView mRegisterDetailsPhone;
    @BindView(R.id.register_details_password1)
    EditText mRegisterDetailsPassword1;
    @BindView(R.id.register_details_password2)
    EditText mRegisterDetailsPassword2;
    @BindView(R.id.register_details_register)
    Button mRegisterDetailsRegister;
    @BindView(R.id.register_details_title)
    TextView mRegisterDetailsTitle;
    @BindView(R.id.register_details_title1)
    TextView mRegisterDetailsTitle1;
    private String mPhone;
    private String mPassword1;
    private String mPassword2;
    private int mType;
    private String mCodeSms;//验证码只有在重置密码是有用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("phone");
        mType = intent.getIntExtra("type", 9);
        mCodeSms = intent.getStringExtra("code");
        initView();
    }

    @NonNull
    @Override
    public RegisterDetailsPresenter createPresenter() {
        return new RegisterDetailsPresenter(this);
    }

    private void initView() {
        mRegisterDetailsPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword1 = mRegisterDetailsPassword1.getText().toString().trim();
                mPassword2 = mRegisterDetailsPassword1.getText().toString().trim();
                if (StringUtils.isPassword(mPassword1, mPassword2)) {
                    mRegisterDetailsIv.setImageResource(R.mipmap.password_yes);
                }
            }
        });
        if (Constant.CODE_PASSWORD == mType) {
            //说明是重置密码  将页面的信息改为重置密码
            mRegisterDetailsTitle.setText("重置你的密码");
            mRegisterDetailsTitle1.setText("的账户重置密码。");
            mRegisterDetailsRegister.setText("重置");
        }
    }

    @OnClick(R.id.register_details_register)
    public void onViewClicked() {
        mPassword1 = mRegisterDetailsPassword1.getText().toString().trim();
        mPassword2 = mRegisterDetailsPassword1.getText().toString().trim();
        if (StringUtils.isPassword(mPassword1, mPassword2)) {
            getPresenter().Register(mPhone, mPassword1,mType,mCodeSms);
        }
    }

    @Override
    public void RegisterSuccess() {
        if (Constant.CODE_REGISTER==mType) {
            startActivity(new Intent(this, MainActivity.class));
        }else if(Constant.CODE_PASSWORD==mType){
            //重置密码成功
            Toast.makeText(App.getContext(),"密码重置成功,请重新登陆",Toast.LENGTH_LONG).show();
            MyService.logOut(this,Constant.LOG_OUT_NOSHOW);
        }
    }

    @Override
    public void RegisterErro(String e) {
        Toast.makeText(App.getContext(),e,Toast.LENGTH_LONG).show();
    }
}
