package com.yuntian.youth.My.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.yuntian.youth.My.presenter.MyDataPresenter;
import com.yuntian.youth.My.view.callback.MyDataView;
import com.yuntian.youth.R;
import com.yuntian.youth.Utils.DialogUtil;
import com.yuntian.youth.Utils.stausbar.StatusBarCompat;
import com.yuntian.youth.listener.impl.YouthListerenImpl;
import com.yuntian.youth.register.model.bean.User;
import com.yuntian.youth.widget.TitleBar;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huxianguang on 2017/5/23.
 */

public class MyDataActivity extends MvpActivity<MyDataView, MyDataPresenter> implements DatePickerDialog.OnDateSetListener, MyDataView {

    public static final int MY_DATA_USERNAME = 0;

    @BindView(R.id.my_data_title)
    TitleBar mMyDataTitle;
    @BindView(R.id.my_information_username)
    TextView mMyInformationUsername;
    @BindView(R.id.my_information_btn_usernam)
    AutoRelativeLayout mMyInformationBtnUsernam;
    @BindView(R.id.my_information_gender)
    TextView mMyInformationGender;
    @BindView(R.id.my_information_btn_gender)
    AutoRelativeLayout mMyInformationBtnGender;
    @BindView(R.id.my_information_age)
    TextView mMyInformationAge;
    @BindView(R.id.my_information_btn_age)
    AutoRelativeLayout mMyInformationBtnAge;
    @BindView(R.id.my_information_save)
    AutoLinearLayout mMyInformationSave;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(false, this, getColor(R.color.green_main));
        initView();


    }

    @NonNull
    @Override
    public MyDataPresenter createPresenter() {
        return new MyDataPresenter(this);
    }

    private void initView() {
        initTitle();
        User user = User.getCurrentUser();
        mMyInformationUsername.setText(user.getUsername());
        mMyInformationGender.setText(user.getGender());
        mMyInformationAge.setText(user.getBirthday());
    }

    private void initTitle() {
        mMyDataTitle.setLeftImageResource(R.mipmap.left);
        mMyDataTitle.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMyDataTitle.setTitle("我的资料");
        mMyDataTitle.setTitleColor(Color.WHITE);
    }

    @OnClick({R.id.my_information_btn_usernam, R.id.my_information_btn_gender, R.id.my_information_btn_age, R.id.my_information_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_information_btn_usernam:
                DialogUtil.showEditDialog(this, MY_DATA_USERNAME, youthListeren);
                break;
            case R.id.my_information_btn_gender:
                String[] item = {"男", "女"};
                DialogUtil.showChoosePicDialog(this, item, "选择性别", youthListeren);
                break;
            case R.id.my_information_btn_age:
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
                datePickDialog.setYearRange(1965, 2016);
                datePickDialog.show(this.getSupportFragmentManager(), "date");
                break;
            case R.id.my_information_save:
                String username = mMyInformationUsername.getText().toString();
                String gender = mMyInformationGender.getText().toString().trim();
                String birthday = mMyInformationAge.getText().toString().trim();
                getPresenter().updateUserData(username, gender, birthday);
                break;
        }
    }

    YouthListerenImpl youthListeren = new YouthListerenImpl() {
        @Override
        public void dialogCall(String content, int type) {
            switch (type) {
                case MY_DATA_USERNAME://username
                    mMyInformationUsername.setText(content);
                    break;
            }
        }

        @Override
        public void dialogCall(DialogInterface dialog, int which) {
            switch (which) {
                case 0://男
                    mMyInformationGender.setText("男");
                    break;
                case 1://女
                    mMyInformationGender.setText("女");
                    break;
            }
        }
    };

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String birthday = year + "-" + (month + 1) + "-" + day;
        mMyInformationAge.setText(birthday);
    }

    @Override
    public void UpdateSuccess() {
        finish();
    }

    @Override
    public void UpdateErro(String erro) {
        Toast.makeText(this, erro, Toast.LENGTH_LONG).show();
    }
}
