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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.api.model.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfLearningFragment extends Fragment {

    @BindView(R.id.subject_spinner)
    Spinner subjectSpinner;

    @BindView(R.id.grade_spinner)
    Spinner gradeSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

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

        //Temp
        List<String> topics = Arrays.asList("Topic", "English", "Marathi", "Maths");
        List<String> languages = Arrays.asList("Language", "1", "2", "3");

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, topics);
        subjectSpinner.setAdapter(subjectAdapter);
        subjectSpinner.setPrompt(topics.get(0));

        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, languages);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setPrompt(languages.get(0));

        searchSelfLearning("108100", "101100", "", 0);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }

    private void searchSelfLearning(String fileType, String language, String topic, final int pageNumber) {
        Logger.d(" searching ");
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(UserDetailUtils.getUserId(getContext()),
                fileType, language, topic);
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
                    //errorView.setVisibility(View.VISIBLE);
                    //errorView.setText(error);
                }

            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });
    }

}
