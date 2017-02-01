package net.mavericklabs.mitra.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.GenericListDataModel;
import net.mavericklabs.mitra.model.database.DbUser;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.List;

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

    @BindView(R.id.loading_panel)
    RelativeLayout loadingPanel;

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

        languageList = CommonCodeUtils.getAppLanguages();
        languageSpinner.setAdapter(new SpinnerArrayAdapter(getContext(),R.layout.custom_spinner_dropdown_item
                ,languageList));

        int languageCode = LanguageUtils.getCurrentLanguage();
        int languageIndex = 0;
        int i = 0;
        for(CommonCode language : languageList) {
            if (language.getCodeID().equals(languageCode)) {
                languageIndex = i;
            }
            i++;
        }

        final int currentLanguageIndex = languageIndex;

        languageSpinner.setSelection(currentLanguageIndex ,false);
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
                                            String token = UserDetailUtils.getToken(getContext());
                                            loadingPanel.setVisibility(View.VISIBLE);
                                            RestClient.getApiService(token).saveLanguage(code.getCodeID())
                                                    .enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                                                        @Override
                                                        public void onResponse(
                                                                Call<BaseModel<GenericListDataModel>> call,
                                                                Response<BaseModel<GenericListDataModel>> response) {
                                                            loadingPanel.setVisibility(View.GONE);
                                                            if(response.isSuccessful()) {
                                                                setDefaultLanguage(code);
                                                            } else {
                                                                languageSpinner.setSelection(currentLanguageIndex,false);
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<BaseModel<GenericListDataModel>> call,
                                                                              Throwable t) {
                                                            loadingPanel.setVisibility(View.GONE);
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

        LanguageUtils.setLocale(language, getContext());

        //Goto Home Activity
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        getActivity().finishAffinity();

        //save new preferred language to database
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DbUser> dbUser = realm.where(DbUser.class).findAll();
        DbUser user = dbUser.get(0);
        realm.beginTransaction();
        user.setPreferredLanguage(code.getCodeID());
        realm.insertOrUpdate(user);
        realm.commitTransaction();
    }
}
