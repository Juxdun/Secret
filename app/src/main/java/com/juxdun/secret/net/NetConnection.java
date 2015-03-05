package com.juxdun.secret.net;

import android.os.AsyncTask;
import android.util.Log;

import com.juxdun.secret.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * 网络通信基类
 * Created by Juxdun on 2015/2/25.
 */
public class NetConnection {

    public NetConnection(final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback, final String... kvs) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                StringBuffer paramsStr = new StringBuffer();
                for (int i = 0; i < kvs.length; i += 2) {
                    paramsStr.append(kvs[i]).append("=").append(kvs[i + 1]).append("&");
                }

                try {
                    // 使用URLConnecttion进行网络连接
                    URLConnection uc;
                    // 根据通信方式进行不同的操作
                    switch (method) {
                        // POST方式
                        case POST:
                            uc = new URL(url).openConnection();
                            uc.setDoOutput(true);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), Config.CHARSET));
                            bw.write(paramsStr.toString());
                            bw.flush();
                            break;
                        // GET方式
                        default:
                            uc = new URL(url + "?" + paramsStr.toString()).openConnection();
                            break;
                    }

                    Log.i(Config.LOG_TAG, "Request URL:" + uc.getURL());
                    Log.i(Config.LOG_TAG, "Request Data:" + paramsStr);

                    // 读取服务器返回结果
                    BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), Config.CHARSET));
                    String line = null;
                    StringBuffer result = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    Log.i(Config.LOG_TAG, "Result:" + result.toString());

                    return result.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // 完成后调用回调方法
                if (null != result) {
                    if (null != successCallback) {
                        successCallback.onSuccess(result);
                    }
                } else {
                    if (null != failCallback) {
                        failCallback.onFail();
                    }
                }
                super.onPostExecute(result);
            }
        }.execute();
    }

    public static interface SuccessCallback {
        void onSuccess(String result);
    }

    public static interface FailCallback {
        void onFail();
    }
}
