/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.CommonCodeWrapper;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLanguageActivity extends AppCompatActivity {

    @BindView(R.id.select_english_button)
    Button selectEnglish;

    @BindView(R.id.select_marathi_button)
    Button selectMarathi;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.select_english_button) void selectEnglish() {
        Logger.d("selected english");
        setLocale("en");
    }

    @OnClick(R.id.select_marathi_button) void selectMarathi() {
        setLocale("mr");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Logger.d("toolbar is set " + getSupportActionBar());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        RealmResults<CommonCode> commonCodes = Realm.getDefaultInstance()
                .where(CommonCode.class).findAll();
        String codeVersion;
        if(commonCodes.isEmpty()) {
            codeVersion = "0";
        } else {
            codeVersion = MitraSharedPreferences.readFromPreferences(getApplicationContext(),"code_version","0");
        }

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
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<CommonCodeWrapper>> call, Throwable t) {
                Logger.d(" on failure ");
            }
        });
    }

    private void setLocale(String lang) {
        LanguageUtils.setLocale(lang, getApplicationContext());

        RealmResults<CommonCode> commonCodes = Realm.getDefaultInstance()
                .where(CommonCode.class).findAll();
        if(commonCodes.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.error_code_list,Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(SelectLanguageActivity.this,ChooseSignInOrRegisterActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
