package com.juxdun.secret.net;

import com.juxdun.secret.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取验证码
 * Created by Juxdun on 2015/2/26.
 */
public class GetCode {

    public GetCode(String phone, final SuccessCallback successCallback, final FailCallback failCallback) {

        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    switch (jsonObj.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (null != successCallback) {
                                successCallback.onSuccess();
                            }
                            break;
                        case Config.RESULT_STATUS_FAIL:
                            if (null != failCallback) {
                                failCallback.onFail();
                            }
                            break;

                    }

                } catch (JSONException e) {

                    if (null != failCallback) {
                        failCallback.onFail();
                    }
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (null != failCallback) {
                    failCallback.onFail();
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_GET_CODE, Config.KEY_PHONE_NUM, phone);
    }

    public static interface SuccessCallback {
        void onSuccess();
    }

    public static interface FailCallback {
        void onFail();
    }

}
