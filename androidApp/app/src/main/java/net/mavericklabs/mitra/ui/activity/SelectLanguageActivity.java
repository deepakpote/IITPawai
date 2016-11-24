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

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.utils.Logger;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
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
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setLocale(String lang) {

        Call<List<CommonCode>> commonCodeList = RestClient.getApiService("").getCodeNameList();

        commonCodeList.enqueue(new Callback<List<CommonCode>>() {
            @Override
            public void onResponse(Call<List<CommonCode>> call, Response<List<CommonCode>> response) {
                if(response.isSuccessful()) {
                    Realm realm = Realm.getDefaultInstance();
                    Logger.d(" realm : " + realm.getPath());
                    List<CommonCode> responseList = response.body();
                    realm.beginTransaction();
                    realm.copyToRealm(responseList);
                    realm.commitTransaction();
                    for (CommonCode commonCode : responseList) {
                        Logger.d(" " + commonCode.getCodeID() + " " + commonCode.getCodeNameEnglish());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CommonCode>> call, Throwable t) {

            }
        });

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);

        Intent intent = new Intent(SelectLanguageActivity.this,ChooseSignInOrRegisterActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
