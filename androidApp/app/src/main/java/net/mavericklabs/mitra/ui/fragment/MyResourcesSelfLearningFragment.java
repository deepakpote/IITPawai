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

package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.SavedContentRequest;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyResourcesSelfLearningFragment extends Fragment {
    @BindView(R.id.topic_spinner)
    Spinner topicSpinner;

    @BindView(R.id.language_spinner)
    Spinner languageSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    @BindView(R.id.error_view)
    TextView errorView;

    @BindView(R.id.loading_panel)
    RelativeLayout loadingPanel;

    private ContentVerticalCardListAdapter adapter;
    private MyResourcesFragment fragment;

    public MyResourcesSelfLearningFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MyResourcesSelfLearningFragment newInstance() {
        MyResourcesSelfLearningFragment fragment = new MyResourcesSelfLearningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Logger.d("fragment -  on permission result");
        adapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_resources_self_learning, container, false);
        ButterKnife.bind(this, rootView);

        fragment = (MyResourcesFragment) getParentFragment();

        final List<CommonCode> topics = new ArrayList<>(CommonCodeUtils.getTopics());
        final List<CommonCode> languages = new ArrayList<>(CommonCodeUtils.getLanguages());

        //Header - not a valid value
        topics.add(0, new CommonCode("", "","Topic", "Topic", 0));
        languages.add(0,new CommonCode("","","Language","Language",0));

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(),
                R.layout.custom_spinner_item_header,
                topics);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        topicSpinner.setAdapter(adapter);
        topicSpinner.setSelection(0 ,false);


        topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonCode language = (CommonCode) languageSpinner.getSelectedItem();
                loadMySelfLearning(language.getCodeID(), topics.get(i).getCodeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerArrayAdapter languageAdapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                languages);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(0 ,false);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonCode topic = (CommonCode) topicSpinner.getSelectedItem();
                loadMySelfLearning(languages.get(i).getCodeID() , topic.getCodeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String language = "";

        RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();
        if(dbUser.size() == 1) {
            DbUser user = dbUser.get(0);
            language = user.getPreferredLanguage();
        }

        loadMySelfLearning(language, "");

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }

    private void loadMySelfLearning(String language, String topic) {
        loadingPanel.setVisibility(View.VISIBLE);

        SavedContentRequest contentRequest = new SavedContentRequest(UserDetailUtils.getUserId(getContext()),
                Constants.ContentTypeSelfLearning);
        RestClient.getApiService("").getSavedContent(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                loadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    Logger.d(" Succes");
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        Logger.d(" contents " + contents.size());

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        contentRecyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new ContentVerticalCardListAdapter(getContext(), contents, fragment);
                        contentRecyclerView.setAdapter(adapter);

                        fragment.subtitle1.setText(getResources().getQuantityString(R.plurals.resources_saved, contents.size(), contents.size()));

                        return;

                    }
                }


                    String error = CommonCodeUtils.getObjectFromCode(HttpUtils.getErrorMessage(response)).getCodeNameForCurrentLocale();
                    Logger.d(" error " + error);
                    contentRecyclerView.setVisibility(View.GONE);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(error);


            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });

    }
}
