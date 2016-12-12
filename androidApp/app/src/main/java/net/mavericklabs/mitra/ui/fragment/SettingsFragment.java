package net.mavericklabs.mitra.ui.fragment;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amoghpalnitkar on 12/9/16.
 */

public class SettingsFragment extends Fragment {
    public SettingsFragment() {
        super();
    }

    @BindView(R.id.language_spinner)
    Spinner languageSpinner;

    private List<CommonCode> languageList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        languageList = CommonCodeUtils.getLanguages();
        languageSpinner.setAdapter(new SpinnerArrayAdapter(getContext(),R.layout.custom_spinner_dropdown_item
                ,languageList));
        final int currentLanguageIndex = getCurrentLanguageIndex();
        languageSpinner.setSelection(currentLanguageIndex, false);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                if(currentLanguageIndex != i) {
                    AlertDialog dialog =
                            new AlertDialog.Builder(getActivity())
                                    .setMessage(getString(R.string.language_select_confirmation))
                                    .setPositiveButton(getString(R.string.yes),
                                            new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int j) {
                                            //make server call - on success
                                            final CommonCode code = languageList.get(i);
                                            String userId = UserDetailUtils.getUserId(getContext());
                                            RestClient.getApiService("").saveLanguage(userId,code.getCodeID())
                                                    .enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                                                        @Override
                                                        public void onResponse(
                                                                Call<BaseModel<GenericListDataModel>> call,
                                                                Response<BaseModel<GenericListDataModel>> response) {
                                                            if(response.isSuccessful()) {
                                                                setDefaultLanguage(code);
                                                            } else {
                                                                languageSpinner.setSelection(currentLanguageIndex,false);
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<BaseModel<GenericListDataModel>> call,
                                                                              Throwable t) {
                                                            languageSpinner.setSelection(currentLanguageIndex,false);
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.cancel),
                                            new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            languageSpinner.setSelection(currentLanguageIndex,false);
                                        }
                                    })
                                    .create();
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setDefaultLanguage(CommonCode code) {
        String language = "en";
        if(code.getCodeNameEnglish().equals("English")) {
            language = "en";
        } else if(code.getCodeNameEnglish().equals("Marathi")) {
            language = "mr";
        }

        Locale myLocale = new Locale(language);
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

        //save new preferred language to database
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DbUser> dbUser = realm.where(DbUser.class).findAll();
        DbUser user = dbUser.get(0);
        realm.beginTransaction();
        user.setPreferredLanguage(code.getCodeID());
        realm.insertOrUpdate(user);
        realm.commitTransaction();
    }

    private int getCurrentLanguageIndex() {
        RealmResults<DbUser> dbUser = Realm.getDefaultInstance().where(DbUser.class).findAll();
        DbUser user = dbUser.get(0);
        int i = 0;
        for(CommonCode language : languageList) {
            if (language.getCodeID().equals(user.getPreferredLanguage())) {
                return i;
            }
            i++;
        }
        return 0;
    }
}
