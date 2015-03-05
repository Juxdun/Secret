package com.juxdun.secret.atys;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juxdun.secret.Config;
import com.juxdun.secret.R;
import com.juxdun.secret.net.Comment;
import com.juxdun.secret.net.GetComment;
import com.juxdun.secret.net.PubComment;
import com.juxdun.secret.tools.MD5Tool;

import java.util.List;

/**
 * 消息Activity
 * Created by Juxdun on 2015/2/26.
 */
public class AtyMessage extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_message);

        adapter = new MessageCommentListAdapter(this);
        setListAdapter(adapter);

        tvMessage = (TextView) findViewById(R.id.tvMessage);
        etComment = (EditText) findViewById(R.id.etComment);

        phone_md5 = getIntent().getStringExtra(Config.KEY_PHONE_MD5);
        msg = getIntent().getStringExtra(Config.KEY_MSG);
        msgId = getIntent().getStringExtra(Config.KEY_MSG_ID);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);

        tvMessage.setText(msg);

        getComments();


        final ProgressDialog pd = ProgressDialog.show(this, "Connecting", "Connecting ,Please wait.");
        findViewById(R.id.btnSendComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etComment.getText())){
                    Toast.makeText(AtyMessage.this,R.string.message_content_can_not_be_empty , Toast.LENGTH_LONG).show();
                    return;
                }
                new PubComment(MD5Tool.md5(Config.getCachedPhoneNum(AtyMessage.this )), token, etComment.getText().toString(), msgId, new PubComment.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();

                        etComment.setText("");

                        getComments();
                    }
                }, new PubComment.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        pd.dismiss();

                        if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN){
                            startActivity(new Intent(AtyMessage.this, AtyLogin.class));
                            finish();
                        }else {

                            Toast.makeText(AtyMessage.this,R.string.fail_to_pub_comment , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 拿到评论并更新界面
     */
    private void getComments() {
        final ProgressDialog pd = ProgressDialog.show(this, "Connecting", "Connecting ,Please wait.");
        new GetComment(phone_md5, token, msgId, 1, 20, new GetComment.SuccessCallback() {
            @Override
            public void onSuccess(String msgId, int page, int perpage, List<Comment> comments) {
                pd.dismiss();

                adapter.clear();
                adapter.addAll(comments);
            }
        }, new GetComment.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                pd.dismiss();
                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN){
                    startActivity(new Intent(AtyMessage.this, AtyLogin.class));
                    finish();
                }else {
                    Toast.makeText(AtyMessage.this, R.string.fail_to_get_comment, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private TextView tvMessage;
    private EditText etComment;
    private String phone_md5, msg, msgId, token;
    private MessageCommentListAdapter adapter;
}
