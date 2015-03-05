package com.juxdun.secret.net;

import com.juxdun.secret.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取消息列表通信类
 * Created by Juxdun on 2015/2/27.
 */
public class Timeline {

    public Timeline(String phone_md5, String token, int page, int perpage, final SuccessCallback successCallback, final FailCallback failCallback){

        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)){
                        case Config.RESULT_STATUS_SUCCESS:
                            if (null != successCallback){
                                List<Message> msgs = new ArrayList<Message>();
                                JSONArray mseJsonArray = obj.getJSONArray(Config.KEY_ITEMS);
                                JSONObject msgObj;
                                for (int i = 0; i < mseJsonArray.length(); i++) {
                                    msgObj = mseJsonArray.getJSONObject(i);
                                    msgs.add(new Message(
                                            msgObj.getString(Config.KEY_MSG_ID),
                                            msgObj.getString(Config.KEY_MSG),
                                            msgObj.getString(Config.KEY_PHONE_MD5)));
                                }

                                successCallback.onSuccess(obj.getInt(Config.KEY_PAGE), obj.getInt(Config.KEY_PERPAGE), msgs);
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if (null != failCallback){
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        case Config.RESULT_STATUS_FAIL:
                            if (null != failCallback){
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;

                    }

                } catch (JSONException e) {

                    if (null != failCallback){
                        failCallback.onFail(Config.RESULT_STATUS_FAIL);
                    }
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (null != failCallback){
                    failCallback.onFail(Config.RESULT_STATUS_FAIL);
                }
            }
        },Config.KEY_ACTION, Config.ACTION_TIMELINE,
                Config.KEY_PHONE_MD5, phone_md5,
                Config.KEY_TOKEN, token,
                Config.KEY_PAGE, page+"",
                Config.KEY_PERPAGE, perpage+"");
    }


    public static interface SuccessCallback{
        void onSuccess(int page, int perpage, List<Message> timeline);
    }

    public static interface FailCallback{
        void onFail(int errorCode);
    }
}
