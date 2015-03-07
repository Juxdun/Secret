package com.juxdun.secret.atys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.juxdun.secret.Config;
import com.juxdun.secret.R;
import com.juxdun.secret.net.Publish;

/**
 * 发布消息Activity
 * Created by Juxdun on 2015/2/26.
 */
public class AtyPublish extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_publish);
        etMsgContent = (EditText) findViewById(R.id.etMsgContent);

        phone_md5 = getIntent().getStringExtra(Config.KEY_PHONE_MD5);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);

        findViewById(R.id.btnPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etMsgContent.getText())) {
                    Toast.makeText(AtyPublish.this, R.string.message_content_can_not_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog pd = ProgressDialog.show(AtyPublish.this, "Connecting", "Connecting ,Please wait.");
                new Publish(phone_md5, token, etMsgContent.getText().toString(), new Publish.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        pd.dismiss();

                        setResult(Config.ACTIVITY_RESULT_NEED_REFRESH);

                        Toast.makeText(AtyPublish.this, R.string.suc_to_publish, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, new Publish.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        pd.dismiss();

                        if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN){
                            startActivity(new Intent(AtyPublish.this, AtyLogin.class));
                            finish();
                        }else {

                            Toast.makeText(AtyPublish.this, R.string.fail_to_publish, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


    private EditText etMsgContent;
    private String phone_md5, token, msg;
}
