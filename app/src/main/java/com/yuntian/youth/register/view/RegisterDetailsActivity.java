package com.yuntian.youth.register.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.StringUtils;
import com.yuntian.youth.mian.view.MainActivity;
import com.yuntian.youth.register.presenter.RegisterDetailsPresenter;
import com.yuntian.youth.register.view.callback.RegisterDetailsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterDetailsActivity extends MvpActivity<RegisterDetailsView,RegisterDetailsPresenter> implements RegisterDetailsView{

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
    private String mPhone;
    private String mPassword1;
    private String mPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        mPhone = intent.getStringExtra("phone");
        initView();
    }

    @NonNull
    @Override
    public RegisterDetailsPresenter createPresenter() {
        return new RegisterDetailsPresenter();
    }

    private void initView() {
        mRegisterDetailsPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword1 = mRegisterDetailsPassword1.getText().toString().trim();
                mPassword2 = mRegisterDetailsPassword1.getText().toString().trim();
                if (StringUtils.isPassword(mPassword1, mPassword2)){
                    mRegisterDetailsIv.setImageResource(R.mipmap.password_yes);
                }
            }
        });
    }

    @OnClick(R.id.register_details_register)
    public void onViewClicked() {
        mPassword1=mRegisterDetailsPassword1.getText().toString().trim();
        mPassword2=mRegisterDetailsPassword1.getText().toString().trim();
        if (StringUtils.isPassword(mPassword1,mPassword2)){
            getPresenter().Register(mPhone,mPassword1);
        }
    }

    @Override
    public void RegisterSuccess() {
        startActivity(new Intent(this, MainActivity.class));

    }

    @Override
    public void RegisterErro(String e) {

    }
}
