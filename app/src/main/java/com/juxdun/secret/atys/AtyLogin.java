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
import com.juxdun.secret.net.GetCode;
import com.juxdun.secret.net.Login;
import com.juxdun.secret.tools.MD5Tool;

/**
 * 登录Activity
 * Created by Juxdun on 2015/2/25.
 */
public class AtyLogin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        etPhone = (EditText) findViewById(R.id.etPhoneNum);
        etCode = (EditText) findViewById(R.id.etCode);

        findViewById(R.id.btnGetCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 如果号码为空，弹出提示
                if (TextUtils.isEmpty(etPhone.getText())) {
                    Toast.makeText(AtyLogin.this, getResources().getString(R.string.phone_num_can_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog pd = ProgressDialog.show(AtyLogin.this, "connecting", "getting code");
                new GetCode(etPhone.getText().toString(), new GetCode.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AtyLogin.this, getResources().getString(R.string.suc_to_get_code), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }, new GetCode.FailCallback() {
                    @Override
                    public void onFail() {
                        Toast.makeText(AtyLogin.this, getResources().getString(R.string.fail_to_get_code), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                });
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果号码为空，弹出提示
                if (TextUtils.isEmpty(etPhone.getText())) {
                    Toast.makeText(AtyLogin.this, getResources().getString(R.string.phone_num_can_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(etCode.getText())) {
                    Toast.makeText(AtyLogin.this, getResources().getString(R.string.code_can_not_be_empty), Toast.LENGTH_LONG).show();
                }

                final ProgressDialog pd = ProgressDialog.show(AtyLogin.this, "connecting", "getting code");
                new Login(MD5Tool.md5(etPhone.getText().toString()), etCode.getText().toString(), new Login.SuccessCallback() {
                    @Override
                    public void onSuccess(String token) {
                        pd.dismiss();
                        // 缓存Token
                        Config.cacheToken(AtyLogin.this, token);
                        // 缓存电话号码
                        Config.cachePhoneNum(AtyLogin.this, etPhone.getText().toString());

                        // 启动Timeline
                        Intent i = new Intent(AtyLogin.this, AtyTimeline.class);
                        i.putExtra(Config.KEY_TOKEN, token);
                        i.putExtra(Config.KEY_PHONE_NUM, etPhone.getText().toString());
                        startActivity(i);

                        // 关闭当前Activity
                        finish();
                    }
                }, new Login.FailCallback() {
                    @Override
                    public void onFail() {
                        pd.dismiss();

                        Toast.makeText(AtyLogin.this, getResources().getString(R.string.fail_to_login), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private EditText etPhone, etCode;
}
