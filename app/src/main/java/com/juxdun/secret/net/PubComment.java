package com.juxdun.secret.net;

import com.juxdun.secret.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 发表评论通信类
 * Created by ju on 15/3/2.
 */
public class PubComment {
    /**
     *
     * @param phone_md5 电话号码的md5
     * @param token token
     * @param content 评论内容
     * @param msgId 消息的Id
     */
    public PubComment(String phone_md5, String token, String content, String msgId, final SuccessCallback successCallback, final FailCallback failCallback){
        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback(){

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)){
                        case Config.RESULT_STATUS_SUCCESS:
                            if (null != successCallback){
                                successCallback.onSuccess();
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if (null != failCallback){
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if (null!=failCallback){
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (null != failCallback){
                        failCallback.onFail(Config.RESULT_STATUS_FAIL);
                    }
                }
            }
        }, new NetConnection.FailCallback(){
            @Override
            public void onFail() {
                if (null != failCallback){
                    failCallback.onFail(Config.RESULT_STATUS_FAIL);
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_PUB_COMMENT,
                Config.KEY_PHONE_MD5, phone_md5,
                Config.KEY_TOKEN, token,
                Config.KEY_CONTENT, content,
                Config.KEY_MSG_ID, msgId);

    }

    public static interface SuccessCallback{
        void onSuccess();
    }
    public static interface FailCallback{
        void onFail(int errorCode);
    }
}
