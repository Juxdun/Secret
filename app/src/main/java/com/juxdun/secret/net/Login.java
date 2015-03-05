package com.juxdun.secret.net;

import com.juxdun.secret.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录通信类
 * Created by Juxdun on 2015/2/26.
 */
public class Login {

    public Login(String phone_md5, String code, final SuccessCallback successCallback, final FailCallback failCallback){
        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)){
                        case Config.RESULT_STATUS_SUCCESS:
                            if (null != successCallback){
                                successCallback.onSuccess(obj.getString(Config.KEY_TOKEN));
                            }
                            break;
                        default:
                            if (null != failCallback){
                                failCallback.onFail();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (null != failCallback){
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTIOM_LOGIN, Config.KEY_PHONE_MD5, phone_md5, Config.KEY_CODE, code);
    }

    public static interface SuccessCallback{
        void onSuccess(String token);
    }

    public static interface FailCallback{
        void onFail();
    }
}
