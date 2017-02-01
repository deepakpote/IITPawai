package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.CommonCodeWrapper;
import net.mavericklabs.mitra.model.database.Migration;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraApplication;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(new Migration()) // Migration to run
                .build();

        Realm.setDefaultConfiguration(config);

        RealmResults<CommonCode> commonCodes = Realm.getDefaultInstance()
                .where(CommonCode.class).findAll();
        final String codeVersion;
        if(!commonCodes.isEmpty()) {
            codeVersion = MitraSharedPreferences.readFromPreferences(getApplicationContext(),"code_version","0");
            load(codeVersion);
        } else {
            codeVersion = "0";
            final EditText serverURL = (EditText) findViewById(R.id.server_url);
            serverURL.setText(MitraSharedPreferences.readFromPreferences(getApplicationContext(),"base_url",
                    "http://54.152.74.194:8000/"));

            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MitraSharedPreferences.saveToPreferences(getApplicationContext(), "base_url", serverURL.getText().toString());
                    load(codeVersion);
                }
            });
            serverURL.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void load(String codeVersion) {
        Call<BaseModel<CommonCodeWrapper>> codeNameListCall = RestClient.getApiService("").getCodeNameList(codeVersion);
        codeNameListCall.enqueue(new Callback<BaseModel<CommonCodeWrapper>>() {
            @Override
            public void onResponse(Call<BaseModel<CommonCodeWrapper>> call, Response<BaseModel<CommonCodeWrapper>> response) {
                if(response.isSuccessful()) {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<CommonCode> commonCodes = Realm.getDefaultInstance()
                            .where(CommonCode.class).findAll();
                    CommonCodeWrapper wrapper = response.body().getData().get(0);
                    if(commonCodes.isEmpty()) {
                        List<CommonCode> responseList = wrapper.getCommonCode();
                        realm.beginTransaction();
                        realm.copyToRealm(responseList);
                        realm.commitTransaction();
                        for (CommonCode commonCode : responseList) {
                            Logger.d(" " + commonCode.getCodeID() + " " + commonCode.getCodeNameEnglish());
                        }
                        MitraSharedPreferences.saveToPreferences(getApplicationContext(),"code_version",wrapper.getVersion());
                    } else {
                        String localVersion = MitraSharedPreferences.readFromPreferences(getApplicationContext(),"code_version","0");
                        String serverVersion = wrapper.getVersion();
                        Logger.d("local version : " + localVersion);
                        Logger.d("server version : " + serverVersion);
                        if(!serverVersion.equalsIgnoreCase(localVersion)) {
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(wrapper.getCommonCode());
                            realm.commitTransaction();
                            MitraSharedPreferences.saveToPreferences(getApplicationContext(),"code_version",serverVersion);
                        }
                    }

                    int languageCode = LanguageUtils.getCurrentLanguage();
                    String languageName = CommonCodeUtils.getObjectFromCode(languageCode).getCodeNameEnglish();
                    Logger.d(" code " + CommonCodeUtils.getObjectFromCode(languageCode));
                    String lang = "en";
                    if(languageName.equals("Marathi")) {
                        lang = "mr";
                    }

                    LanguageUtils.setLocale(lang, getApplicationContext());

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
                } else {
                    Toast.makeText(SplashActivity.this, getString(R.string.error_code_list), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<CommonCodeWrapper>> call, Throwable t) {
                Logger.d(" on failure ");
                Toast.makeText(SplashActivity.this, getString(R.string.error_code_list), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
