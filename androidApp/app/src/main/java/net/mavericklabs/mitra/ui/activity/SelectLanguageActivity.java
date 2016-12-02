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

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.Logger;

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
    }

    private void setLocale(String lang) {

        Call<BaseModel<CommonCode>> codeNameListCall = RestClient.getApiService("").getCodeNameList();

        codeNameListCall.enqueue(new Callback<BaseModel<CommonCode>>() {
            @Override
            public void onResponse(Call<BaseModel<CommonCode>> call, Response<BaseModel<CommonCode>> response) {
                if(response.isSuccessful()) {
                    Logger.d(" is successful");
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<CommonCode> commonCodes = Realm.getDefaultInstance()
                            .where(CommonCode.class).findAll();
                    if(commonCodes.isEmpty()) {
                        List<CommonCode> responseList = response.body().getData();
                        realm.beginTransaction();
                        realm.copyToRealm(responseList);
                        realm.commitTransaction();
                        for (CommonCode commonCode : responseList) {
                            Logger.d(" " + commonCode.getCodeID() + " " + commonCode.getCodeNameEnglish());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<CommonCode>> call, Throwable t) {
                Logger.d(" on failure ");
            }
        });

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }

        //Deprecated api - but still works. workaround is complicated
        res.updateConfiguration(conf, dm);

        Intent intent = new Intent(SelectLanguageActivity.this,ChooseSignInOrRegisterActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
