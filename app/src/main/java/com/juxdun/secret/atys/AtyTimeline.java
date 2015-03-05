package com.juxdun.secret.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.juxdun.secret.Config;
import com.juxdun.secret.R;
import com.juxdun.secret.ld.MyContacts;
import com.juxdun.secret.net.Message;
import com.juxdun.secret.net.Timeline;
import com.juxdun.secret.net.UploadContacts;
import com.juxdun.secret.tools.MD5Tool;

import java.util.List;

/**
 * 消息列表Activity
 * Created by Juxdun on 2015/2/25.
 */
public class AtyTimeline extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_timeline);

        adapter = new TimelineMessageListAdapter(this);
        setListAdapter(adapter);

        phone_num = getIntent().getStringExtra(Config.KEY_PHONE_NUM);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);
        phone_md5 = MD5Tool.md5(phone_num);

        final ProgressDialog pd = ProgressDialog.show(this, "Connecting", "Connecting ,Please wait.");
        new UploadContacts(MD5Tool.md5(phone_num), token, MyContacts.getContactsJSONString(this), new UploadContacts.SuccessCallback() {
            @Override
            public void onSuccess() {
                loadMessage();

                pd.dismiss();
            }
        }, new UploadContacts.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(AtyTimeline.this, AtyLogin.class));
                    finish();
                } else {
                    loadMessage();
                }
            }
        });
    }

    // 加载消息列表
    private void loadMessage() {
        Log.i(Config.LOG_TAG, ">>>>>>>>>>>>success to loadMessage()");
        final ProgressDialog pd = ProgressDialog.show(this, "Connecting", "Connecting ,Please wait.");

        new Timeline(phone_md5, token, 1, 20, new Timeline.SuccessCallback() {
            @Override
            public void onSuccess(int page, int perpage, List<Message> timeline) {
                pd.dismiss();

                adapter.addAll(timeline);
            }
        }, new Timeline.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();

                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) { // 如果是无效的token
                    startActivity(new Intent(AtyTimeline.this, AtyLogin.class));
                    finish();
                } else { //如果是失败
                    Toast.makeText(AtyTimeline.this, R.string.fail_to_load_timeline_data, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Message msg = adapter.getItem(position);
        Intent i = new Intent(AtyTimeline.this, AtyMessage.class);
        i.putExtra(Config.KEY_MSG, msg.getMsg());
        i.putExtra(Config.KEY_MSG_ID, msg.getMsgId());
        i.putExtra(Config.KEY_PHONE_MD5, msg.getPhone_md5());
        i.putExtra(Config.KEY_TOKEN, token);
        startActivity(i);
    }

    private String phone_num, token, phone_md5;
    private TimelineMessageListAdapter adapter = null;
}
