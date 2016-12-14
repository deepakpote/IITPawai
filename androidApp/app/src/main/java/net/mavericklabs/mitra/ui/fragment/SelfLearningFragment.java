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

public class SelfLearningFragment extends Fragment {

    @BindView(R.id.subject_spinner)
    Spinner topicSpinner;

    @BindView(R.id.grade_spinner)
    Spinner languageSpinner;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    @BindView(R.id.error_view)
    TextView errorView;

    @BindView(R.id.loading_panel)
    RelativeLayout loadingPanel;

    @BindView(R.id.filter_recycler_view)
    RecyclerView filterRecyclerView;

    @BindView(R.id.view_below_filter_list)
    View viewBelowFilterList;

    private ContentVerticalCardListAdapter adapter;

    private List<BaseObject> filterList;
    private List<CommonCode> filterTopicList;
    private ChipLayoutAdapter filterAdapter;
    private String language;


    public SelfLearningFragment() {
        //mandatory constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Logger.d("fragment -  on permission result");
        adapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

        filterList = new ArrayList<>();
        filterTopicList = new ArrayList<>();

        filterAdapter = new ChipLayoutAdapter(filterList);
        filterAdapter.setShowRemoveButton(true);
        filterAdapter.setListener(new OnChipRemovedListener() {
            @Override
            public void onChipRemoved(int position) {
                BaseObject object = filterList.get(position);
                CommonCode commonCode = object.getCommonCode();
                if(commonCode.getCodeGroupID().equals(CommonCodeGroup.TOPICS)) {
                    filterTopicList.remove(commonCode);
                } else {
                    language = "";
                }
                filterList.remove(position);
                filterAdapter.notifyItemRemoved(position);
                if(filterList.isEmpty()) {
                    viewBelowFilterList.setVisibility(GONE);
                }
                searchSelfLearning( 0);
            }
        });

        filterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        filterRecyclerView.setVisibility(GONE);


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
                filterTopicList.add(topics.get(i));
                searchSelfLearning(0);

                filterRecyclerView.setVisibility(View.VISIBLE);
                viewBelowFilterList.setVisibility(View.VISIBLE);
                filterList.add(new BaseObject(topics.get(i), true));
                filterAdapter.setObjects(filterList);
                filterRecyclerView.swapAdapter(filterAdapter, false);
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
        if(StringUtils.isEmpty(language)) {
            RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                    .where(DbUser.class).findAll();
            if(dbUser.size() == 1) {
                DbUser user = dbUser.get(0);
                language = user.getPreferredLanguage();
                Logger.d(" language " + language);
            }
        }
        String topicList = CommonCodeUtils.getCommonCodeCommaSeparatedList(filterTopicList);
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(UserDetailUtils.getUserId(getContext()),
                 language, topicList);
        contentRequest.setPageNumber(pageNumber);
        RestClient.getApiService("").searchSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                loadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    Logger.d(" Succes");
                    if(response.body().getData() != null) {
                        contentRecyclerView.setVisibility(View.VISIBLE);
                        errorView.setVisibility(View.GONE);

                        List<Content> contents = response.body().getData();
                        Logger.d(" contents " + contents.size());

                        if(pageNumber == 0) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            contentRecyclerView.setLayoutManager(linearLayoutManager);
                            adapter = new ContentVerticalCardListAdapter(getContext(), contents, SelfLearningFragment.this);
                            contentRecyclerView.setAdapter(adapter);
                        } else {
                            ContentVerticalCardListAdapter adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                            List<Content> originalContents = adapter.getContents();
                            Logger.d(" original contents " + originalContents.size());
                            originalContents.addAll(contents);
                            adapter = new ContentVerticalCardListAdapter(getContext(), originalContents, SelfLearningFragment.this);
                            contentRecyclerView.swapAdapter(adapter, false);
                        }

                        contentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                                    Logger.d(" scrolled idle");
                                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                    int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                                    int childCount = contentRecyclerView.getAdapter().getItemCount();

                                    Logger.d(" lastVisibleItem " + lastVisibleItem  + " childCount " + childCount);
                                    if(lastVisibleItem == childCount - 1) {
                                        searchSelfLearning(1);
                                    }
                                }
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                            }
                        });

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
