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
import net.mavericklabs.mitra.api.model.SavedSelfLearningRequest;
import net.mavericklabs.mitra.api.model.SavedTeachingAidsRequest;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;
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


public class MyResourcesSelfLearningFragment extends BaseContentFragment {
    @BindView(R.id.topic_spinner)
    Spinner topicSpinner;

    @BindView(R.id.language_spinner)
    Spinner languageSpinner;

    private MyResourcesFragment fragment;
    private List<CommonCode> filterTopicList, filterLanguageList;

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

        filterTopicList = new ArrayList<>();
        filterLanguageList = new ArrayList<>();

        setupFilterView(new OnChipRemovedListener() {
            @Override
            public void onChipRemoved(int position) {
                BaseObject object = filterList.get(position);
                CommonCode commonCode = object.getCommonCode();
                if(commonCode.getCodeGroupID().equals(CommonCodeGroup.TOPICS)) {
                    filterTopicList.remove(commonCode);
                } else if(commonCode.getCodeGroupID().equals(CommonCodeGroup.LANGUAGE)){
                    filterLanguageList.remove(commonCode);
                }
                removeFromFilterList(position);
                loadMySelfLearning();
            }
        });

        final List<CommonCode> topics = new ArrayList<>(CommonCodeUtils.getTopics());
        final List<CommonCode> languages = new ArrayList<>(CommonCodeUtils.getLanguages());

//        //Header - not a valid value
        topics.add(0, new CommonCode(0, 0,"Topic", "Topic", 0));
        languages.add(0,new CommonCode(0,0,"Language","Language",0));

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(),
                R.layout.custom_spinner_item_header,
                topics);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        topicSpinner.setAdapter(adapter);
        topicSpinner.setSelection(0 ,false);


        topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(topics.get(i).getCodeID() != 0) {
                    filterTopicList.add(topics.get(i));
                    addItemToFilterList(topics.get(i));
                    loadMySelfLearning();
                    topicSpinner.setSelection(0 ,false);
                }
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
                if(languages.get(i).getCodeID() != 0) {
                    filterLanguageList.add(languages.get(i));
                    addItemToFilterList(languages.get(i));
                    loadMySelfLearning();
                    languageSpinner.setSelection(0 ,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadMySelfLearning();


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }

    private void loadMySelfLearning() {
        loadingPanel.setVisibility(View.VISIBLE);

        String topicList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterTopicList);
        String languageList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterLanguageList);


        SavedSelfLearningRequest contentRequest = new SavedSelfLearningRequest(Constants.ContentTypeSelfLearning,
                                                        languageList, topicList);
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).getSavedSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                loadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    Logger.d(" Succes");
                    List<Content> contents = response.body().getData();
                    if(contents != null && contents.size() != 0) {
                        Logger.d(" contents " + contents.size());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        contentRecyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new ContentVerticalCardListAdapter(getContext(), contents, fragment);
                        adapter.setShowDeleteOption(true);
                        contentRecyclerView.setAdapter(adapter);
                        contentRecyclerView.setVisibility(View.VISIBLE);
                        errorView.setVisibility(View.GONE);

                        fragment.subtitle1.setText(getResources().getQuantityString(R.plurals.resources_saved, contents.size(), contents.size()));

                    } else {

                        //Get from body
                        String error = CommonCodeUtils.getObjectFromCode(response.body().getResponseMessage()).getCodeNameForCurrentLocale();
                        Logger.d(" error " + error);
                        contentRecyclerView.setVisibility(View.GONE);
                        errorView.setVisibility(View.VISIBLE);
                        errorView.setText(error);

                        fragment.subtitle1.setText(getResources().getQuantityString(R.plurals.resources_saved, 0, 0));
                    }

                    return;
                }


                //Get from error body
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
