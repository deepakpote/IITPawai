package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.iid.FirebaseInstanceId;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int languageCode = LanguageUtils.getCurrentLanguage();
        RealmResults<CommonCode> commonCodes = Realm.getDefaultInstance()
                .where(CommonCode.class).findAll();
        if(!commonCodes.isEmpty()) {
            String languageName = CommonCodeUtils.getObjectFromCode(languageCode).getCodeNameEnglish();
            Logger.d(" code " + CommonCodeUtils.getObjectFromCode(languageCode));
            String lang = "en";
            if(languageName.equals("Marathi")) {
                lang = "mr";
            }

            LanguageUtils.setLocale(lang, getApplicationContext());
        }


        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    String phoneNumber = UserDetailUtils.getMobileNumber(getApplicationContext());

                    //case 1 : user has not entered his phone number
                    if(StringUtils.isEmpty(phoneNumber)) {
                        Intent selectLanguage = new Intent(SplashActivity.this,SelectLanguageActivity.class);
                        startActivity(selectLanguage);
                        finishAffinity();
                    } else { // user has entered phone number ..

                        //case 2: but has not verified his phone number
                        if(!UserDetailUtils.isVerifiedMobileNumber(getApplicationContext())) {
                            Intent verifyOtp = new Intent(SplashActivity.this,VerifyOtpActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone_number",phoneNumber);
                            boolean signIn= MitraSharedPreferences.readFromPreferences(
                                                                    getApplicationContext(),
                                                                    "sign_in",Boolean.FALSE);
                            bundle.putBoolean("is_from_sign_in",signIn);
                            verifyOtp.putExtras(bundle);
                            startActivity(verifyOtp);
                            finishAffinity();
                        } else { // has verified his phone number
                            boolean hasEnteredInformation = UserDetailUtils.hasEnteredInformation(getApplicationContext());

                            // case 3 : not yet entered personal information
                            if(!hasEnteredInformation) {
                                Intent selectLanguage = new Intent(SplashActivity.this,EditProfileActivity.class);
                                startActivity(selectLanguage);
                                finishAffinity();

                            } else { // case 4 : everything good to go. take user home :)
                                Intent selectLanguage = new Intent(SplashActivity.this,HomeActivity.class);
                                startActivity(selectLanguage);
                                finishAffinity();
                            }
                        }
                    }
                }
            }
        };
        timerThread.start();
    }
}
