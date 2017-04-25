package com.yuntian.youth.Utils;

/**
 * Created by huxianguang on 2017/4/17.
 */

public class StringUtils {
    public static boolean isPhone(String phone){
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (phone.length()==11) {
            //matches():字符串是否在给定的正则表达式匹配
            return phone.matches(num);
        }
        return false;
    }
    public static boolean isPassword(String password1,String password2){
        if (password1.length()>6&&password2.equals(password1)){
            return true;
        }
        return false;
    }
}
