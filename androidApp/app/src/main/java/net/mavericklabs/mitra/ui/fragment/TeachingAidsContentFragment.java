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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.TeachingAidsContentRequest;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishakha on 27/02/17.
 */

public class TeachingAidsContentFragment extends BaseContentFragment {

    @BindView(R.id.subject_spinner)
    Spinner subjectSpinner;

    @BindView(R.id.grade_spinner)
    Spinner gradeSpinner;

    private FilterChangeListener filterChangeListener;
    private int tabNumber;

    public TeachingAidsContentFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TeachingAidsContentFragment newInstance(int tabNumber) {
        TeachingAidsContentFragment fragment = new TeachingAidsContentFragment();
        Bundle args = new Bundle();
        args.putInt("tabNumber", tabNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public interface FilterChangeListener {

        void addGradeToFilter(CommonCode commonCode);
        void addSubjectToFilter(CommonCode commonCode);
        void removeGradeFromFilter(CommonCode commonCode);
        void removeSubjectFromFilter(CommonCode commonCode);
        void setChanged(Boolean changed);
        Boolean isChanged();
        List<CommonCode> getSubjects();
        List<CommonCode> getGrades();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            filterChangeListener = (FilterChangeListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement FilterChangeListener");
        }

    }

    private boolean setFiltersAndSearch(boolean onCreate) {
        if(filterChangeListener != null && (filterChangeListener.isChanged() || onCreate)) {
            clearFilterList();
            List<CommonCode> gradeList = filterChangeListener.getGrades();
            Logger.d(" gradelist " + gradeList.size() + " tab " + tabNumber);
            for(CommonCode grade : gradeList) {
                addGradeToView(grade);
            }

            List<CommonCode> subjectList = filterChangeListener.getSubjects();
            Logger.d(" subjectList " + subjectList.size());
            for (CommonCode subject : subjectList) {
                addSubjectToView(subject);
            }

            //The changed content has been consumed. Set Changed to false
            filterChangeListener.setChanged(false);

            searchTeachingAids(getFileType(), 0);
            return true;
        }

        return false;
    }

    private Integer getFileType() {
        return CommonCodeUtils.getFileTypeAtPosition(tabNumber).getCodeID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teaching_aids_list, container, false);
        ButterKnife.bind(this, rootView);

        tabNumber = getArguments().getInt("tabNumber");

        setupFilterView(new OnChipRemovedListener() {
            @Override
            public void onChipRemoved(int position) {
                BaseObject object = filterList.get(position);
                CommonCode commonCode = object.getCommonCode();
                if(commonCode.getCodeGroupID().equals(CommonCodeGroup.SUBJECTS)) {
                    filterChangeListener.removeSubjectFromFilter(commonCode);
                } else {
                    filterChangeListener.removeGradeFromFilter(commonCode);
                }
                removeFromFilterList(position);
                searchTeachingAids(getFileType(), 0);
            }
        });

        final List<CommonCode> subjects = new ArrayList<>(CommonCodeUtils.getSubjects());
        final List<CommonCode> grades = new ArrayList<>(CommonCodeUtils.getGrades());

        //Header - not a valid value
        subjects.add(0, new CommonCode(0, 0,getString(R.string.subject), getString(R.string.subject), 0));
        grades.add(0,new CommonCode(0,0,getString(R.string.grade),getString(R.string.grade),0));

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                subjects);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
        subjectSpinner.setSelection(0 ,false);


        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(subjects.get(i).getCodeID() != 0) {
                    filterChangeListener.addSubjectToFilter(subjects.get(i));
                    addSubjectToView(subjects.get(i));
                    searchTeachingAids(getFileType(), 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerArrayAdapter gradeAdapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                grades);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);
        gradeSpinner.setSelection(0 ,false);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(grades.get(i).getCodeID() != 0) {
                    filterChangeListener.addGradeToFilter(grades.get(i));
                    addGradeToView(grades.get(i));
                    searchTeachingAids(getFileType() , 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //If set filters and search has not completed successfully, search with no filters
        if(!setFiltersAndSearch(true)) {
            searchTeachingAids(getFileType(), 0);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }

    private void addSubjectToView(CommonCode subject) {
        addItemToFilterList(subject);
        subjectSpinner.setSelection(0 ,false);
    }

    private void addGradeToView(CommonCode grade) {
        addItemToFilterList(grade);
        gradeSpinner.setSelection(0 ,false);
    }

    private void searchTeachingAids(final int fileType, final int pageNumber) {
        Logger.d(" searching ");
        if(pageNumber == 0) {
            contentRecyclerView.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.VISIBLE);
        }

        String subjectList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterChangeListener.getSubjects());
        String gradeList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterChangeListener.getGrades());

        TeachingAidsContentRequest contentRequest = new TeachingAidsContentRequest(fileType,
                subjectList, gradeList);
        contentRequest.setPageNumber(pageNumber);
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).searchTeachingAids(LanguageUtils.getCurrentLanguage(), contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                loadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    loadContent(response, pageNumber, new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if(isNextPageToBeLoaded(newState, recyclerView)) {
                                //On Scroll, show loading panel at bottom
                                adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                                adapter.showLoading();

                                searchTeachingAids(fileType, 1);
                            }
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });

                    return;
                }

                if(pageNumber == 0) {
                    String error = CommonCodeUtils.getObjectFromCode(HttpUtils.getErrorMessage(response)).getCodeNameForCurrentLocale();
                    Logger.d(" error " + error);
                    contentRecyclerView.setVisibility(View.GONE);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(error);
                } else {
                    adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                    adapter.stopLoading();
                }

            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail " + t.getMessage());
                if(t instanceof ConnectException) {
                    Toast.makeText(getContext(), getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

                if(pageNumber > 0) {
                    adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                    adapter.stopLoading();
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.d(" isVisible " + isVisibleToUser + tabNumber);
        if(isVisibleToUser) {
            setFiltersAndSearch(false);
        }
    }
}
