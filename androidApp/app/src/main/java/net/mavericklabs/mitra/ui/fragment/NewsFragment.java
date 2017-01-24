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
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.ui.adapter.NewsListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.DisplayUtils;
import net.mavericklabs.mitra.utils.HttpUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amoghpalnitkar on 14/11/16.
 */

public class NewsFragment extends Fragment{

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


    public NewsFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Logger.d("News on view created ");

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
        Logger.d("News on resume ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class NewsContentFragment extends BaseContentFragment {

        NewsListAdapter adapter;

        @BindView(R.id.date_from_spinner)
        TextView dateFromSpinner;

        @BindView(R.id.date_to_spinner)
        TextView dateToSpinner;

        private int department;
        String fromDate, toDate;

        public NewsContentFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static NewsContentFragment newInstance(int tabNumber) {
            NewsContentFragment fragment = new NewsContentFragment();
            Bundle args = new Bundle();
            args.putInt("tabNumber", tabNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
            ButterKnife.bind(this, rootView);

            int tabNumber = getArguments().getInt("tabNumber");
            final Integer fileType = CommonCodeUtils.getFileTypeAtPosition(tabNumber).getCodeID();
            //department = CommonCodeUtils.getFileTypeAtPosition(tabNumber).getCodeID();

            setupFilterView(new OnChipRemovedListener() {
                @Override
                public void onChipRemoved(int position) {
                    BaseObject object = filterList.get(position);
                    if(object.getCommonCode().getCodeGroupID().equals(CommonCodeGroup.FROM_DATE)) {
                        if(!StringUtils.isEmpty(toDate)) {
                            fromDate = "-";
                            CommonCode code = new CommonCode(0, object.getCommonCode().getCodeGroupID(),
                                    "-", "-", 0);
                            setItemInFilterList(code, 0);
                        } else {
                            fromDate = "";
                            removeFromFilterList(position);
                        }

                    } else {
                        toDate = "";
                        removeFromFilterList(position);
                    }
                    searchNews();
                }
            });
            filterAdapter.setWidth(getResources().getDisplayMetrics().widthPixels / 2
                    - DisplayUtils.dpToPx(24, getContext()));

            searchNews();

            dateFromSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialog(CommonCodeGroup.FROM_DATE);
                }
            });

            dateToSpinner.setEnabled(false);
            dateToSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialog(CommonCodeGroup.TO_DATE);
                }
            });

            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        private void showDatePickerDialog(final int codeGroup) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogLayout = layoutInflater.inflate(R.layout.dialog_date_picker,null);
            final DatePicker datePicker = (DatePicker) dialogLayout.findViewById(R.id.date_picker);

            Calendar calendar = Calendar.getInstance();

            //Set Max date to today
            datePicker.setMaxDate(calendar.getTimeInMillis());
            if(codeGroup == CommonCodeGroup.FROM_DATE) {
                //Set max date for "From" to "to" date
                if(!StringUtils.isEmpty(toDate)) {
                    Date date = DateUtils.convertToDate(toDate, "d MMM yyyy");
                    if(date != null) {
                        Logger.d(" setting max date " + date);
                        datePicker.setMaxDate(date.getTime());
                    }
                }

                if(!StringUtils.isEmpty(fromDate) && !fromDate.equals("-")) {
                    Date date = DateUtils.convertToDate(fromDate, "d MMM yyyy");
                    calendar.setTime(date);
                }

            } else {
                //Set min date for "To" to "from" date
                if(!StringUtils.isEmpty(fromDate) && !fromDate.equals("-")) {
                    Date date = DateUtils.convertToDate(fromDate, "d MMM yyyy");
                    if(date != null) {
                        Logger.d(" setting min date " + date);
                        datePicker.setMinDate(date.getTime());
                    }
                }

                if(!StringUtils.isEmpty(toDate)) {
                    Date date = DateUtils.convertToDate(toDate, "d MMM yyyy");
                    calendar.setTime(date);
                }
            }


            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            Button doneButton = (Button) dialogLayout.findViewById(R.id.done_button);

            final AlertDialog datePickerDialog = new AlertDialog.Builder(getContext())
                    .setView(dialogLayout)
                    .create();
            datePickerDialog.show();
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    String date = DateUtils.convertToString(calendar.getTime(), "d MMM yyyy");
                    CommonCode code = new CommonCode(0, codeGroup, date, date, 0);
                    if(codeGroup == CommonCodeGroup.FROM_DATE) {
                        Logger.d("  fromdate " + fromDate);
                        if(StringUtils.isEmpty(fromDate)) {
                            addItemToFilterList(code);
                        } else {
                            setItemInFilterList(code, 0);
                        }

                        fromDate = date;
                        dateToSpinner.setEnabled(true);
                    } else {
                        if(StringUtils.isEmpty(toDate)) {
                            addItemToFilterList(code);
                        } else {
                            setItemInFilterList(code, 1);
                        }
                        toDate = date;
                    }

                    datePickerDialog.dismiss();
                    searchNews();
                }
            });
        }

        private void searchNews() {
            Logger.d(" searching ");

            Realm realm = Realm.getDefaultInstance();
            RealmResults<News> dbNews = realm.where(News.class).findAll();

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            contentRecyclerView.setLayoutManager(layoutManager);
            NewsListAdapter newsListAdapter = new NewsListAdapter(getContext(), dbNews);
            contentRecyclerView.setAdapter(newsListAdapter);



//            loadingPanel.setVisibility(View.VISIBLE);
//            contentRecyclerView.setVisibility(View.GONE);
//
//            RestClient.getApiService("").listNews().enqueue(new Callback<BaseModel<News>>() {
//                @Override
//                public void onResponse(Call<BaseModel<News>> call, Response<BaseModel<News>> response) {
//                    if(response.isSuccessful()) {
//                        loadingPanel.setVisibility(View.GONE);
//                        contentRecyclerView.setVisibility(View.VISIBLE);
//                        List<News> news = response.body().getData();
//                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                        contentRecyclerView.setLayoutManager(layoutManager);
//                        NewsListAdapter newsListAdapter = new NewsListAdapter(getContext(), news);
//                        contentRecyclerView.setAdapter(newsListAdapter);
//                    } else {
//                        String error = CommonCodeUtils.getObjectFromCode(HttpUtils.getErrorMessageForNews(response)).getCodeNameForCurrentLocale();
//                        loadingPanel.setVisibility(View.GONE);
//                        errorView.setVisibility(View.VISIBLE);
//                        errorView.setText(error);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<BaseModel<News>> call, Throwable t) {
//                    loadingPanel.setVisibility(View.GONE);
//                }
//            });
        }
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a NewsContentFragment (defined as a static inner class below).
            return NewsContentFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            Logger.d(" pages " + CommonCodeUtils.getFileTypeCount());
            return CommonCodeUtils.getFileTypeCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Logger.d(" title " + CommonCodeUtils.getFileTypeAtPosition(position).getCodeNameForCurrentLocale());
            return CommonCodeUtils.getFileTypeAtPosition(position).getCodeNameForCurrentLocale();
        }

    }


}
