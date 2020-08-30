package com.quriosity.quriosity.WelcomeSlide;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.quriosity.quriosity.activity.DashboardMainActivity;
import com.quriosity.quriosity.login.SignupActivity;
import com.quriosity.quriosity.utils.FirebaseUtil;
import com.quriosity.quriosity.utils.WedAmorApplication;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FirebaseUtil(WelcomeActivity.this);

        if (((WedAmorApplication) getApplicationContext()).getCheckFirstInstall()) {
            if (((WedAmorApplication) getApplicationContext()).getCheckLogin()) {
                startActivity(new Intent(this, DashboardMainActivity.class));
            } else startActivity(new Intent(this, SignupActivity.class));
        } else {
            startActivity(new Intent(WelcomeActivity.this, IntroActivity.class));
        }
        finish();
    }

}