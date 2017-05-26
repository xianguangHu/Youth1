package com.yuntian.youth.global;

/**
 * Created by huxianguang on 2017/4/15.
 */

public class Constant {
    public static final String APPLICATION_ID="53b822335f4af2bbdd4348428a40df53";
    public static final String REST_APP_KEY="175a02024c0b98dd89c6f91c9b67623c";


    public static final int CROP_SMALL_PICTURE = 2;//头像剪裁后标签

    //高德地图LBS云key
    public static final String GDLBS_KEY="6f0bbb13017361d20b3dd3cd21b3858f";
    //高德云图表id
    public static final String GDYUN_ID="58feb01b2376c11620d1ce62";
    //高德位置属性type 1表示上传经纬度
    public static final int GDLOCTYPE=1;

    //点赞和睬
    public static final int LIKE_TYPE_ONE=1;
    public static final int LIKE_TYPE_TWO=2;
    public static final int UNLIKE_TYPE_ONE=-1;
    public static final int UNLIKE_TYPE_TWO=-2;

    //及时通讯
    public static final int CHAT_MESSAGE_TYPE_MY=1001;
    public static final int CHAT_MESSAGE_TYPE_FRIENDS=1002;

    //性别
    public static final int GEBDER_MAN=1;//男
    public static final int GENDER_WOMAN=0;//女

    //注册和更换手机号  在验证码页面用到
    public static final int CODE_REGISTER=0;//注册
    public static final int CODE_REPLACE=1;//更换手机号
    public static final int CODE_PASSWORD=2;//修改密码

    //退出登陆
    public static final int LOG_OUT_SHOW=0;
    public static final int LOG_OUT_NOSHOW=1;

}
