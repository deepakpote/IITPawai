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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    int currentLocale = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.language_picker);
        Button button = (Button) findViewById(R.id.button);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("locale")) {
            currentLocale = bundle.getInt("locale");
        }
        Log.d("TAG", " launch "  + currentLocale);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(currentLocale);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AcceptPhoneNumberActivity.class);
                startActivity(intent);
            }
        });

//        LocaleList locales = getResources().getConfiguration().getLocales();
//        for (int i = 0 ;i < locales.size() ; i++) {
//            Locale locale = locales.get(i);
//            Log.d("TAG", " tag  "+ locale.toLanguageTag());
//        }

//        Locale locale = getResources().getConfiguration().getLocales().get(0);
//        Log.d("TAG", locale.toLanguageTag());
//
//        Locale locale1 = getResources().getConfiguration().getLocales().get(1);
//        Log.d("TAG", locale1.toLanguageTag());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(currentLocale != position) {
                    currentLocale = position;
                    Log.d("TAG", " selected"  + currentLocale);
                    setLocale();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setLocale() {
        String lang = "";
        switch (currentLocale) {
            case 0 :
                lang = "en";
                break;
            case 1:
                lang = "mr";
                break;
        }

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        Log.d("TAG", " Intent"  + currentLocale);
        bundle.putInt("locale", currentLocale);
        refresh.putExtras(bundle);
        startActivity(refresh);
        finish();
    }
}
