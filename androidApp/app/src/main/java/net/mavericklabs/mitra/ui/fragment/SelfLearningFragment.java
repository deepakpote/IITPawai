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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfLearningFragment extends Fragment {

    @BindView(R.id.subject_spinner)
    Spinner topicSpinner;

    @BindView(R.id.grade_spinner)
    Spinner languageSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    @BindView(R.id.error_view)
    TextView errorView;

    private ContentVerticalCardListAdapter adapter;


    public SelfLearningFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_self_learning,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

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
                searchSelfLearning(language.getCodeID(), topics.get(i).getCodeID(), 0);
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
                searchSelfLearning(languages.get(i).getCodeID() , topic.getCodeID(), 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchSelfLearning("101100", "", 0);

        contentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                int childCount = recyclerView.getChildCount();

                if(lastVisibleItem == childCount) {
                    CommonCode topic = (CommonCode) topicSpinner.getSelectedItem();
                    CommonCode language = (CommonCode) languageSpinner.getSelectedItem();
                    searchSelfLearning(language.getCodeID(), topic.getCodeID(), 1);
                }
            }
        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }

    private void searchSelfLearning(String language, String topic, final int pageNumber) {
        Logger.d(" searching ");
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(UserDetailUtils.getUserId(getContext()),
                 language, topic);
        contentRequest.setPageNumber(pageNumber);
        RestClient.getApiService("").searchSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {

                if(response.isSuccessful()) {
                    Logger.d(" Succes");
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        Logger.d(" contents " + contents.size());

                        if(pageNumber == 0) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            contentRecyclerView.setLayoutManager(linearLayoutManager);
                            adapter = new ContentVerticalCardListAdapter(getContext(), contents);
                            contentRecyclerView.setAdapter(adapter);
                        } else {
                            ContentVerticalCardListAdapter adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                            List<Content> originalContents = adapter.getContents();
                            Logger.d(" original contents " + originalContents.size());
                            originalContents.addAll(contents);
                            adapter = new ContentVerticalCardListAdapter(getContext(), originalContents);
                            contentRecyclerView.swapAdapter(adapter, false);
                        }

                        return;

                    }
                }

                if(pageNumber == 0) {
                    String error = CommonCodeUtils.getObjectFromCode(HttpUtils.getErrorMessage(response)).getCodeNameForCurrentLocale();
                    Logger.d(" error " + error);
                    contentRecyclerView.setVisibility(View.GONE);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(error);
                }

            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });
    }

}
