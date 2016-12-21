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
import android.util.Log;
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
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ChipLayoutAdapter;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
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

import static android.view.View.GONE;

public class SelfLearningFragment extends BaseContentFragment {

    @BindView(R.id.subject_spinner)
    Spinner topicSpinner;

    @BindView(R.id.grade_spinner)
    Spinner languageSpinner;

    private List<CommonCode> filterTopicList;


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

        filterTopicList = new ArrayList<>();
        setupFilterView(new OnChipRemovedListener() {
            @Override
            public void onChipRemoved(int position) {
                BaseObject object = filterList.get(position);
                CommonCode commonCode = object.getCommonCode();
                if(commonCode.getCodeGroupID().equals(CommonCodeGroup.TOPICS)) {
                    filterTopicList.remove(commonCode);
                } else {
                    language = 0;
                }
                removeFromFilterList(position);
                searchSelfLearning( 0);
            }
        });

        final List<CommonCode> topics = new ArrayList<>(CommonCodeUtils.getTopics());
        final List<CommonCode> languages = new ArrayList<>(CommonCodeUtils.getLanguages());

        //Header - not a valid value
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
                    searchSelfLearning(0);
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
                language = languages.get(i).getCodeID();
                searchSelfLearning(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        searchSelfLearning(0);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null) {
            adapter.releaseLoaders();
        }
    }


    private void searchSelfLearning(final int pageNumber) {
        Logger.d(" searching ");
        Logger.d("sent language " + language);
        if(language == 0) {
            RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                    .where(DbUser.class).findAll();
            if(dbUser.size() == 1) {
                DbUser user = dbUser.get(0);
                language = user.getPreferredLanguage();
                Logger.d(" language " + language);
            }
        }
        String topicList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterTopicList);
        
        contentRecyclerView.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.VISIBLE);
//        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(UserDetailUtils.getUserId(getContext()),
//                 language, topicList);
//        contentRequest.setPageNumber(pageNumber);
//        RestClient.getApiService("").searchSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
//            @Override
//            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
//                loadingPanel.setVisibility(View.GONE);
//                if(response.isSuccessful()) {
//
//                    loadContent(response, pageNumber, new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//                            if(newState == RecyclerView.SCROLL_STATE_IDLE) {
//                                Logger.d(" scrolled idle");
//                                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                                int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
//                                int childCount = contentRecyclerView.getAdapter().getItemCount();
//
//                                Logger.d(" lastVisibleItem " + lastVisibleItem  + " childCount " + childCount);
//                                if(lastVisibleItem == childCount - 1) {
//                                    searchSelfLearning(1);
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//
//                        }
//                    });
//                    return;
//                }
//
//                if(pageNumber == 0) {
//                    String error = CommonCodeUtils.getObjectFromCode(HttpUtils.getErrorMessage(response)).getCodeNameForCurrentLocale();
//                    Logger.d(" error " + error);
//                    contentRecyclerView.setVisibility(View.GONE);
//                    errorView.setVisibility(View.VISIBLE);
//                    errorView.setText(error);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
//                Logger.d(" on fail");
//            }
//        });
    }

}
