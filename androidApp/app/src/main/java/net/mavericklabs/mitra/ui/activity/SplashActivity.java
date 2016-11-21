package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //TODO check if user has entered phone number already
                    String phoneNumber = UserDetailUtils.getMobileNumber(getApplicationContext());
                    if(StringUtils.isEmpty(phoneNumber)) {
                        Intent selectLanguage = new Intent(SplashActivity.this,SelectLanguageActivity.class);
                        startActivity(selectLanguage);
                        finishAffinity();
                    } else {
                        //TODO go to main activity
                        Intent selectLanguage = new Intent(SplashActivity.this,SelectLanguageActivity.class);
                        startActivity(selectLanguage);
                        finishAffinity();
                    }
                }
            }
        };
        timerThread.start();
    }
}
