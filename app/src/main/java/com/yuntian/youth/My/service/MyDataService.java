package com.yuntian.youth.My.service;

import com.yuntian.youth.register.model.bean.User;

import rx.Observable;

/**
 * Created by huxianguang on 2017/5/23.
 */

public class MyDataService {

    public static Observable<Void> updateUser(String username,String gender,String birthday,int age){
        User user=new User();
        user.setUsername(username);
        if ("男".equals(gender)){
            user.setGender(1);
        }else if ("女".equals(gender)){
            user.setGender(0);
        }
        user.setAge(age+"");
        user.setBirthday(birthday);
        Observable<Void> observable=user.updateObservable(User.getCurrentUser().getObjectId());
        return observable;
    }
}
