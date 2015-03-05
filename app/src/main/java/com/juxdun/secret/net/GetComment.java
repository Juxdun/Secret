package com.juxdun.secret.net;

import com.juxdun.secret.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取评论通信类
 * Created by Juxdun on 2015/2/28.
 */
public class GetComment {

    public GetComment(String phone_md5, String token, String msgId, int page, int perpage, final SuccessCallback successCallback, final FailCallback failCallback) {

        new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    switch (obj.getInt(Config.KEY_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (null != successCallback) {
                                List<Comment> comments = new ArrayList<Comment>();
                                JSONArray array = obj.getJSONArray(Config.KEY_ITEMS);
                                JSONObject comm;
                                for (int i = 0; i < array.length(); i++) {
                                    comm = array.getJSONObject(i);
                                    comments.add(new Comment(comm.getString(Config.KEY_CONTENT), comm.getString(Config.KEY_PHONE_MD5)));
                                }
                                successCallback.onSuccess(obj.getString(Config.KEY_MSG_ID),
                                        obj.getInt(Config.KEY_PAGE),
                                        obj.getInt(Config.KEY_PERPAGE),
                                        comments);
                            }

                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if (null != failCallback) {
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if (null != failCallback) {
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (null != failCallback) {
                        failCallback.onFail(Config.RESULT_STATUS_FAIL);
                    }
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (null != failCallback) {
                    failCallback.onFail(Config.RESULT_STATUS_FAIL);
                }
            }
        }, Config.KEY_ACTION, Config.ACTION_GET_COMMENT,
                Config.KEY_PHONE_MD5, phone_md5,
                Config.KEY_TOKEN, token,
                Config.KEY_MSG_ID, msgId,
                Config.KEY_PAGE, page + "",
                Config.KEY_PERPAGE, perpage + "");
    }

    public static interface SuccessCallback {
        void onSuccess(String msgId, int page, int perpage, List<Comment> comments);
    }

    public static interface FailCallback {
        void onFail(int errorCode);
    }
}
