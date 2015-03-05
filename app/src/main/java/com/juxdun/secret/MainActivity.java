package com.juxdun.secret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.juxdun.secret.atys.AtyLogin;
import com.juxdun.secret.atys.AtyTimeline;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = Config.getCachedToken(this);
        String phone_num = Config.getCachedPhoneNum(this);
        if (null!=token && null!=phone_num){
            Intent i = new Intent(this, AtyTimeline.class);
            i.putExtra(Config.KEY_TOKEN, token);
            i.putExtra(Config.KEY_PHONE_NUM, phone_num);
            startActivity(i);
        }else {
            startActivity(new Intent(this, AtyLogin.class));
        }

        finish();
    }





}
