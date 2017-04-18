package net.mavericklabs.mitra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.Chapter;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.CommonCodeWrapper;
import net.mavericklabs.mitra.model.database.Migration;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
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
                .schemaVersion(2) // Must be bumped when the schema changes
                .migration(new Migration()) // Migration to run
                .build();

        Realm.setDefaultConfiguration(config);

        loadCommonCodes();
    }

    private void loadCommonCodes() {
        RealmResults<CommonCode> commonCodes = Realm.getDefaultInstance()
                .where(CommonCode.class).findAll();
        String currentCodeVersion;
        if(!commonCodes.isEmpty()) {
            currentCodeVersion = MitraSharedPreferences.readFromPreferences(getApplicationContext(),"code_version","0");
        } else {
            currentCodeVersion = "0";
        }

        final String codeVersion = currentCodeVersion;

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

                    proceed();
                } else if(codeVersion.equals("0")){
                    Toast.makeText(SplashActivity.this, getString(R.string.error_relaunch_app), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                    proceed();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<CommonCodeWrapper>> call, Throwable t) {
                Logger.d(" on failure ");
                Toast.makeText(SplashActivity.this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                proceed();
            }
        });
    }

    private void proceed() {

        final String phoneNumber = UserDetailUtils.getMobileNumber(getApplicationContext());
        int languageCode = LanguageUtils.getCurrentLanguage();
        LanguageUtils.setLocale(languageCode, getApplicationContext());

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

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

                            } else { // case 4 : everything good to go. sync chapters and take user home :)
                                loadChapters();
                            }
                        }
                    }
                }
            }
        };
        timerThread.start();

    }

    private void loadChapters() {
        String token = UserDetailUtils.getToken(getApplicationContext());
        Call<BaseModel<Chapter>> chapterListCall = RestClient.getApiService(token).getChapters("","");

        RealmResults<Chapter> chapters = Realm.getDefaultInstance()
                .where(Chapter.class).findAll();
        final int chapterCount = chapters.size();

        chapterListCall.enqueue(new Callback<BaseModel<Chapter>>() {
            @Override
            public void onResponse(Call<BaseModel<Chapter>> call, Response<BaseModel<Chapter>> response) {

                if(response.isSuccessful()) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body().getData());
                    realm.commitTransaction();
                    gotoHomePage();
                } else {
                    if(chapterCount == 0) {
                        Toast.makeText(SplashActivity.this, getString(R.string.error_relaunch_app), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        gotoHomePage();
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseModel<Chapter>> call, Throwable t) {
                Logger.d(" on failure " + t.getMessage());
                if(chapterCount == 0) {
                    Toast.makeText(SplashActivity.this, getString(R.string.error_relaunch_app), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    gotoHomePage();
                }
            }
        });
    }

    private void gotoHomePage() {
        Intent home = new Intent(SplashActivity.this,HomeActivity.class);
        startActivity(home);
        finishAffinity();
    }

}
