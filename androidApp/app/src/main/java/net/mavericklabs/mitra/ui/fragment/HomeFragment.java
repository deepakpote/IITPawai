package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.News;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.api.model.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.NewsListAdapter;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amoghpalnitkar on 14/11/16.
 */

public class HomeFragment extends Fragment{

    @BindView(R.id.popularTeachingAidsRecyclerView)
    RecyclerView popularVideosRecyclerView;

    @BindView(R.id.popularSelfLearningRecyclerView)
    RecyclerView popularSelfLearningRecyclerView;

    @BindView(R.id.news_recycler_view)
    RecyclerView newsRecyclerView;

    @BindView(R.id.teaching_aids_solid_button)
    Button teachingAidsSolidButton;

    @OnClick(R.id.teaching_aids_solid_button)
    void goToTeachingAids() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu()
                    .getItem(homeActivity.DRAWER_ITEM_TEACHING_AIDS));
        }
    }

    @BindView(R.id.self_learning_solid_button)
    Button selfLearningSolidButton;

    @OnClick(R.id.self_learning_solid_button)
    void goToSelfLearning() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu()
                    .getItem(homeActivity.DRAWER_ITEM_SELF_LEARNING));
        }
    }

    @BindView(R.id.trainings_solid_button)
    Button trainingsSolidButton;

    @OnClick(R.id.trainings_solid_button)
    void goToTrainingCalendar() {
        if(getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.selectDrawerItem(homeActivity.navigationView.getMenu()
                    .getItem(homeActivity.DRAWER_ITEM_TRAINING_CALENDAR));
        }
    }

    @BindView(R.id.teaching_aids_loading_panel)
    RelativeLayout teachingAidsLoadingPanel;

    @BindView(R.id.self_learning_loading_panel)
    RelativeLayout selfLearningLoadingPanel;


    private BaseHorizontalCardListAdapter teachingAidsAdapter, selfLearningAdapter;

    public HomeFragment() {
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        loadPopularTeachingAids();
        loadPopularSelfLearning();
        loadNews();
    }

    private void loadNews() {
        RestClient.getApiService("").listNews().enqueue(new Callback<BaseModel<News>>() {
            @Override
            public void onResponse(Call<BaseModel<News>> call, Response<BaseModel<News>> response) {
                if(response.isSuccessful()) {
                    List<News> news = response.body().getData();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    newsRecyclerView.setLayoutManager(layoutManager);
                    NewsListAdapter newsListAdapter = new NewsListAdapter(getContext(), news);
                    newsRecyclerView.setAdapter(newsListAdapter);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<News>> call, Throwable t) {

            }
        });
    }

    private void loadPopularTeachingAids() {
        //TODO popular resources

        TeachingAidsContentRequest contentRequest = new TeachingAidsContentRequest(108100, "", "");
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).searchTeachingAids(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                teachingAidsLoadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        popularVideosRecyclerView.setLayoutManager(linearLayoutManager);
                        teachingAidsAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
                        popularVideosRecyclerView.setAdapter(teachingAidsAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });
    }

    private void loadPopularSelfLearning() {
        //TODO popular resources
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest("", "");
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).searchSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                selfLearningLoadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                        popularSelfLearningRecyclerView.setLayoutManager(manager);
                        selfLearningAdapter = new BaseHorizontalCardListAdapter(getContext(), contents);
                        popularSelfLearningRecyclerView.setAdapter(selfLearningAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (teachingAidsAdapter != null) {
            teachingAidsAdapter.releaseLoaders();
        }
        if (selfLearningAdapter != null) {
            selfLearningAdapter.releaseLoaders();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if(teachingAidsAdapter != null) {
            teachingAidsAdapter.releaseLoaders();
        }

        if(selfLearningAdapter != null) {
            selfLearningAdapter.releaseLoaders();
        }
    }
}
