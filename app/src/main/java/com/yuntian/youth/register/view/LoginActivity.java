package com.yuntian.youth.register.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.R;
import com.yuntian.youth.mian.view.MainActivity;
import com.yuntian.youth.register.presenter.LoginPresenter;
import com.yuntian.youth.register.view.callback.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpActivity<LoginView,LoginPresenter> implements LoginView {

    @BindView(R.id.login_phone)
    TextView mLoginPhone;
    @BindView(R.id.login_password)
    EditText mLoginPassword;
    @BindView(R.id.login_login)
    Button mLoginLogin;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        mPhone = intent.getStringExtra("phone");
        initView();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    private void initView() {
        mLoginPhone.setText(mPhone);
    }

    @OnClick(R.id.login_login)
    public void onViewClicked() {
        String password=mLoginPassword.getText().toString().trim();
        if (password!=null&&password.length()>6){
            getPresenter().Login(mPhone,password);
        }
    }

    @Override
    public void LoginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void LoginErro(String e) {

    }
}
