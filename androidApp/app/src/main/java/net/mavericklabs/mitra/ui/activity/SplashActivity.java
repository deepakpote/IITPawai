package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.iid.FirebaseInstanceId;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
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
                    String phoneNumber = UserDetailUtils.getEmailAddress(getApplicationContext());
                    if(StringUtils.isEmpty(phoneNumber)) {
                        Intent selectLanguage = new Intent(SplashActivity.this,SelectLanguageActivity.class);
                        startActivity(selectLanguage);
                        finishAffinity();
                    } else {
                        if(!UserDetailUtils.isVerifiedEmailAddress(getApplicationContext())) {
                            Intent verifyOtp = new Intent(SplashActivity.this,VerifyOtpActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone_number",phoneNumber);
                            boolean signIn= MitraSharedPreferences.readFromPreferences(
                                                                    getApplicationContext(),
                                                                    "sign_in",Boolean.FALSE);
                            Logger.d("sign in value : " + signIn);
                            bundle.putBoolean("is_from_sign_in",signIn);
                            verifyOtp.putExtras(bundle);
                            startActivity(verifyOtp);
                            finishAffinity();
                        } else {
                            //TODO check if he has entered his info
                            Intent selectLanguage = new Intent(SplashActivity.this,SelectLanguageActivity.class);
                            startActivity(selectLanguage);
                            finishAffinity();
                        }
                    }
                }
            }
        };
        timerThread.start();
    }
}
