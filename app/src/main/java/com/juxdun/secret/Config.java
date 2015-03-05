package com.juxdun.secret;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置类
 * Created by Juxdun on 2015/2/25.
 */
public class Config {

    public static final String LOG_TAG = "Logging Secret";

    public static final String SERVER_URL = "http://demo.eoeschool.com/api/v1/nimings/io";
//    public static final String SERVER_URL = "http://192.168.1.2:8080/springmvc-1/api.jsp";
    public static final String APP_ID = "com.juxdun.secret";
    public static final String CHARSET = "utf-8";

    public static final String KEY_ACTION = "action";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_PHONE_NUM = "phone";
    public static final String KEY_PHONE_MD5 = "phone_md5";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CODE = "code";
    public static final String KEY_CONTACTS = "contacts";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PERPAGE = "perpage";
    public static final String KEY_ITEMS = "items";
    public static final String KEY_MSG_ID = "msgId";
    public static final String KEY_MSG = "msg";
    public static final String KEY_CONTENT = "content";

    public static final String ACTION_GET_CODE = "send_pass";
    public static final String ACTIOM_LOGIN = "login";
    public static final String ACTION_UPLOAD_CONTACTS = "upload_contacts";
    public static final String ACTION_TIMELINE = "timeline";
    public static final String ACTION_GET_COMMENT = "get_comment";
    public static final String ACTION_PUB_COMMENT = "pub_comment";
    public static final String ACTION_PUBLISH = "publish";

    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID_TOKEN = 2;

    public static final int ACTIVITY_RESULT_NEED_REFRESH = 10000;

    /**
     * 拿到缓存的Token
     * @param context 上下文
     * @return 返回Token
     */
    public static String getCachedToken(Context context){
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
    }

    /**
     * 保存Token到缓存
     * @param context 上下文
     * @param token Token
     */
    public static void cacheToken(Context context, String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    /**
     * 获取缓存手机号
     * @param context 上下文
     * @return 手机号
     */
    public static String getCachedPhoneNum(Context context){
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_PHONE_NUM, null);
    }

    /**
     * 缓存手机号
     * @param context 上下文
     * @param phoneNum 手机号
     */
    public static void cachePhoneNum(Context context, String phoneNum){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_PHONE_NUM, phoneNum);
        editor.commit();
    }
}
