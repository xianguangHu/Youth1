package com.yuntian.youth.Utils;

import com.yuntian.youth.register.model.bean.User;

/**
 * Created by huxianguang on 2017/4/17.
 */

public class StringUtils {
    /**
     * 判断手机号  true表示符合手机号
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (phone.length() == 11) {
            //matches():字符串是否在给定的正则表达式匹配
            return phone.matches(num);
        }
        return false;
    }

    public static boolean isPassword(String password1, String password2) {
        if (password1.length() > 6 && password2.equals(password1)) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户信息是否和原来信息一样  一样返回false  不一样返回true
     * @param username
     * @param gender
     * @param birthday
     * @return
     */
    public static boolean isUserDataUpdate(String username, String gender, String birthday) {
        User user=User.getCurrentUser();
        if (user.getUsername().equals(username)&&user.getBirthday().equals(birthday)&&user.getGender().equals(gender)){
            return false;
        }
        return true;
    }
}
