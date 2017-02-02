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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class SelectLanguageActivity extends BaseActivity {

    @BindView(R.id.select_english_button)
    Button selectEnglish;

    @BindView(R.id.select_marathi_button)
    Button selectMarathi;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.select_english_button) void selectEnglish() {
        Logger.d("selected english");
        setLocale(Constants.AppLanguageEnglish);
    }

    @OnClick(R.id.select_marathi_button) void selectMarathi() {
        setLocale(Constants.AppLanguageMarathi);
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
    }

    private void setLocale(int lang) {
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
