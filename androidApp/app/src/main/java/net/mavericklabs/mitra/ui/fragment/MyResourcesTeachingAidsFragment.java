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
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.SavedTeachingAidsRequest;
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


public class MyResourcesTeachingAidsFragment extends BaseContentFragment {
    @BindView(R.id.subject_spinner)
    Spinner subjectSpinner;

    @BindView(R.id.grade_spinner)
    Spinner gradeSpinner;

    @BindView(R.id.type_spinner)
    Spinner typeSpinner;


    private MyResourcesFragment fragment;
    private List<CommonCode> filterGradeList, filterSubjectList, filterTypeList;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_resources_teaching_aids, container, false);
        ButterKnife.bind(this, rootView);

        fragment = (MyResourcesFragment) getParentFragment();

        filterGradeList = new ArrayList<>();
        filterSubjectList = new ArrayList<>();
        filterTypeList = new ArrayList<>();

        setupFilterView(new OnChipRemovedListener() {
            @Override
            public void onChipRemoved(int position) {
                BaseObject object = filterList.get(position);
                CommonCode commonCode = object.getCommonCode();
                if(commonCode.getCodeGroupID().equals(CommonCodeGroup.SUBJECTS)) {
                    filterSubjectList.remove(commonCode);
                } else if(commonCode.getCodeGroupID().equals(CommonCodeGroup.GRADES)){
                    filterGradeList.remove(commonCode);
                } else {
                    filterTypeList.remove(commonCode);
                }
                removeFromFilterList(position);
                loadMyTeachingAids();
            }
        });

        final List<CommonCode> subjects = new ArrayList<>(CommonCodeUtils.getSubjects());
        final List<CommonCode> grades = new ArrayList<>(CommonCodeUtils.getGrades());
        final List<CommonCode> types = new ArrayList<>(CommonCodeUtils.getFileTypes());

        //Header - not a valid value
        subjects.add(0, new CommonCode(0, 0,getString(R.string.subject), getString(R.string.subject), 0));
        grades.add(0,new CommonCode(0,0,getString(R.string.grade),getString(R.string.grade),0));
        types.add(0,new CommonCode(0,0,getString(R.string.type),getString(R.string.type),0));


        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                subjects);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
        subjectSpinner.setSelection(0 ,false);


        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(subjects.get(i).getCodeID() != 0) {
                    filterSubjectList.add(subjects.get(i));
                    addItemToFilterList(subjects.get(i));
                    loadMyTeachingAids();
                    subjectSpinner.setSelection(0 ,false);
                }
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
                if(grades.get(i).getCodeID() != 0) {
                    filterGradeList.add(grades.get(i));
                    addItemToFilterList(grades.get(i));
                    loadMyTeachingAids();
                    gradeSpinner.setSelection(0 ,false);
                }


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
                if(types.get(i).getCodeID() != 0) {
                    filterTypeList.add(types.get(i));
                    addItemToFilterList(types.get(i));
                    loadMyTeachingAids();
                    typeSpinner.setSelection(0 ,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadMyTeachingAids();


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }

    private void loadMyTeachingAids() {
        loadingPanel.setVisibility(View.VISIBLE);

        String subjectList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterSubjectList);
        String gradeList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterGradeList);
        String typeList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterTypeList);
        //String typeList = "";

        SavedTeachingAidsRequest contentRequest = new SavedTeachingAidsRequest(
                Constants.ContentTypeTeachingAids, typeList, subjectList, gradeList);
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).getSavedTeachingAids(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
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

                        if(fragment.isAdded()) {
                            fragment.subtitle0.setText(getResources().getQuantityString(R.plurals.resources_saved,
                                    contents.size(), contents.size()));
                            contentRecyclerView.setVisibility(View.VISIBLE);
                            errorView.setVisibility(View.GONE);
                        }


                    } else {

                        //Get from body
                        String error = CommonCodeUtils.getObjectFromCode(response.body().getResponseMessage()).getCodeNameForCurrentLocale();
                        Logger.d(" error " + error);
                        contentRecyclerView.setVisibility(View.GONE);
                        errorView.setVisibility(View.VISIBLE);
                        errorView.setText(error);

                        if(fragment.isAdded()) {
                            fragment.subtitle0.setText(getResources().getQuantityString(R.plurals.resources_saved,
                                    0, 0));
                        }

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
