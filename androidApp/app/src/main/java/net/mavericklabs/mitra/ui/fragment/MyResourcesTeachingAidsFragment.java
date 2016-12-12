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
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.SavedContentRequest;
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

import net.mavericklabs.mitra.utils.Logger;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyResourcesTeachingAidsFragment extends Fragment {
    @BindView(R.id.subject_spinner)
    Spinner subjectSpinner;

    @BindView(R.id.grade_spinner)
    Spinner gradeSpinner;

    @BindView(R.id.type_spinner)
    Spinner typeSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    @BindView(R.id.error_view)
    TextView errorView;

    @BindView(R.id.loading_panel)
    RelativeLayout loadingPanel;


    private ContentVerticalCardListAdapter contentAdapter;
    private String language;
    private MyResourcesFragment fragment;

    public MyResourcesTeachingAidsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MyResourcesTeachingAidsFragment newInstance() {
        MyResourcesTeachingAidsFragment fragment = new MyResourcesTeachingAidsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Logger.d("fragment -  on permission result");
        contentAdapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_resources_teaching_aids, container, false);
        ButterKnife.bind(this, rootView);

        final List<CommonCode> subjects = new ArrayList<>(CommonCodeUtils.getSubjects());
        final List<CommonCode> grades = new ArrayList<>(CommonCodeUtils.getGrades());
        final List<CommonCode> types = new ArrayList<>(CommonCodeUtils.getFileTypes());

        //Header - not a valid value
        subjects.add(0, new CommonCode("", "","Subject", "Subject", 0));
        grades.add(0,new CommonCode("","","Grade","Grade",0));
        types.add(0,new CommonCode("","","Type","Type",0));

        fragment = (MyResourcesFragment) getParentFragment();



        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                subjects);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
        subjectSpinner.setSelection(0 ,false);

        RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();
        if(dbUser.size() == 1) {
            DbUser user = dbUser.get(0);
            language = user.getPreferredLanguage();
            Logger.d(" language " + language);
        }


        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonCode grade = (CommonCode) gradeSpinner.getSelectedItem();
                CommonCode fileType = (CommonCode) typeSpinner.getSelectedItem();
                loadMyTeachingAids(fileType.getCodeID(), language, subjects.get(i).getCodeID(), grade.getCodeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerArrayAdapter gradeAdapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                grades);
        gradeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setSelection(0 ,false);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonCode subject = (CommonCode) subjectSpinner.getSelectedItem();
                CommonCode fileType = (CommonCode) typeSpinner.getSelectedItem();
                loadMyTeachingAids(fileType.getCodeID(), language, grades.get(i).getCodeID() , subject.getCodeID());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerArrayAdapter typeAdapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                types);
        typeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setSelection(0 ,false);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommonCode subject = (CommonCode) subjectSpinner.getSelectedItem();
                CommonCode grade = (CommonCode) gradeSpinner.getSelectedItem();
                loadMyTeachingAids(types.get(i).getCodeID(), language, grade.getCodeID() , subject.getCodeID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadMyTeachingAids("", language, "", "");


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(contentAdapter != null) {
            contentAdapter.releaseLoaders();
        }
    }

    private void loadMyTeachingAids(final String fileType, final String language, String subject, String grade) {
        loadingPanel.setVisibility(View.VISIBLE);
        SavedContentRequest contentRequest = new SavedContentRequest(UserDetailUtils.getUserId(getContext()),
                Constants.ContentTypeTeachingAids);
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
                        contentAdapter = new ContentVerticalCardListAdapter(getContext(), contents, fragment);
                        contentRecyclerView.setAdapter(contentAdapter);
                        fragment.subtitle0.setText(getResources().getQuantityString(R.plurals.resources_saved, contents.size()));

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
