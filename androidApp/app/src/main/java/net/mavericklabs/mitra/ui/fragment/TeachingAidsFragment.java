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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.TeachingAidsContentRequest;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.database.DbGrade;
import net.mavericklabs.mitra.model.database.DbSubject;
import net.mavericklabs.mitra.model.database.DbUser;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amoghpalnitkar on 14/11/16.
 */

public class TeachingAidsFragment extends Fragment{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentStatePagerAdapter}.
     */
    private PagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;


    public TeachingAidsFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teaching_aids,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Logger.d("My resources on view created ");

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs_my_resources);
        if(CommonCodeUtils.getFileTypeCount() > 4) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new PagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public void onResume() {
        super.onResume();
        Logger.d("My resources on resume ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class TeachingAidsContentFragment extends BaseContentFragment {

        @BindView(R.id.subject_spinner)
        Spinner subjectSpinner;

        @BindView(R.id.grade_spinner)
        Spinner gradeSpinner;

        private List<CommonCode> filterGradeList, filterSubjectList;

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_teaching_aids_list, container, false);
            ButterKnife.bind(this, rootView);

            int tabNumber = getArguments().getInt("tabNumber");
            final Integer fileType = CommonCodeUtils.getFileTypeAtPosition(tabNumber).getCodeID();

            filterGradeList = new ArrayList<>();
            filterSubjectList = new ArrayList<>();

            setupFilterView(new OnChipRemovedListener() {
                @Override
                public void onChipRemoved(int position) {
                    BaseObject object = filterList.get(position);
                    CommonCode commonCode = object.getCommonCode();
                    if(commonCode.getCodeGroupID().equals(CommonCodeGroup.SUBJECTS)) {
                        filterSubjectList.remove(commonCode);
                    } else {
                        filterGradeList.remove(commonCode);
                    }
                    removeFromFilterList(position);
                    searchTeachingAids(fileType, 0);
                }
            });

            final List<CommonCode> subjects = new ArrayList<>(CommonCodeUtils.getSubjects());
            final List<CommonCode> grades = new ArrayList<>(CommonCodeUtils.getGrades());

            //Header - not a valid value
            subjects.add(0, new CommonCode(0, 0,getString(R.string.subject), getString(R.string.subject), 0));
            grades.add(0,new CommonCode(0,0,getString(R.string.grade),getString(R.string.grade),0));


            //From profile, set initial filters
            RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                    .where(DbUser.class).findAll();
            if(dbUser.size() == 1) {
                DbUser user = dbUser.get(0);
                RealmList<DbSubject> dbSubjects = user.getSubjects();
                for(DbSubject subject : dbSubjects) {
                    addSubject(CommonCodeUtils.getObjectFromCode(subject.getSubjectCommonCode()));
                }

                RealmList<DbGrade> dbGrades = user.getGrades();
                for(DbGrade grade : dbGrades) {
                    addGrade(CommonCodeUtils.getObjectFromCode(grade.getGradeCommonCode()));
                }
            }

            SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(getActivity(), R.layout.custom_spinner_item_header,
                    subjects);
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            subjectSpinner.setAdapter(adapter);
            subjectSpinner.setSelection(0 ,false);


            subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(subjects.get(i).getCodeID() != 0) {
                        addSubject(subjects.get(i));
                        searchTeachingAids(fileType, 0);
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
                        addGrade(grades.get(i));
                        searchTeachingAids(fileType , 0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            searchTeachingAids(fileType, 0);

//            loadMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CommonCode subject = (CommonCode) subjectSpinner.getSelectedItem();
//                    CommonCode grade = (CommonCode) gradeSpinner.getSelectedItem();
//                    searchTeachingAids(fileType, "101100", subject.getCodeID(), grade.getCodeID(), 1);
//                }
//            });

            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if(adapter != null) {
                adapter.releaseLoaders();
            }
        }

        private void addSubject(CommonCode subject) {
            filterSubjectList.add(subject);
            addItemToFilterList(subject);
            subjectSpinner.setSelection(0 ,false);
        }

        private void addGrade(CommonCode grade) {
            filterGradeList.add(grade);
            addItemToFilterList(grade);
            gradeSpinner.setSelection(0 ,false);
        }

        private void searchTeachingAids(final int fileType, final int pageNumber) {
            Logger.d(" searching ");
            if(pageNumber == 0) {
                contentRecyclerView.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.VISIBLE);
            }

            String subjectList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterSubjectList);
            String gradeList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterGradeList);

            TeachingAidsContentRequest contentRequest = new TeachingAidsContentRequest(fileType,
                                                            subjectList, gradeList);
            contentRequest.setPageNumber(pageNumber);
            String token = UserDetailUtils.getToken(getContext());
            RestClient.getApiService(token).searchTeachingAids(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
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
                    Logger.d(" on fail");
                    if(pageNumber > 0) {
                        adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                        adapter.stopLoading();
                    }
                }
            });
        }
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TeachingAidsContentFragment (defined as a static inner class below).
            return TeachingAidsFragment.TeachingAidsContentFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return CommonCodeUtils.getFileTypeCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CommonCodeUtils.getFileTypeAtPosition(position).getCodeNameForCurrentLocale();
        }

    }


}
