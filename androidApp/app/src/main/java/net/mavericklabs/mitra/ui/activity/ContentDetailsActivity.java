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

package net.mavericklabs.mitra.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.api.model.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Requirements;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.RequirementsListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentDetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    @BindView(R.id.similar_contents_recycler_view)
    RecyclerView contentRecyclerView;

    @BindView(R.id.content_card_view)
    CardView contentCardView;

    @BindView(R.id.content_image_view)
    ImageView contentImageView;

    @BindView(R.id.requirements_grid_view)
    RecyclerView requirementsGridView;

    @BindView(R.id.author_name)
    TextView authorName;

    @BindView(R.id.details)
    TextView details;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.requirements_layout)
    LinearLayout requirementsLayout;

    BaseHorizontalCardListAdapter similarContentsAdapter;
    private Content content;
    private YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);

        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        ViewGroup.LayoutParams imageLayoutParams = contentImageView.getLayoutParams();
        imageLayoutParams.height = displayMetrics.heightPixels / 3;
        contentImageView.setLayoutParams(imageLayoutParams);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            content = (Content) bundle.getSerializable("content");
        }

        if(content != null) {
            //Load Video
            if(content.getFileType().equals(Constants.FileTypeVideo)) {

                YouTubePlayerSupportFragment frag =
                        (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
                frag.initialize(Constants.youtubeDeveloperKey, this);

                contentImageView.setVisibility(View.GONE);
            } else {
                //Show file Icon
                contentImageView.setVisibility(View.VISIBLE);
            }

            if(content.getContentTypeCodeID().equals(Constants.ContentTypeTeachingAids)) {

                requirementsLayout.setVisibility(View.VISIBLE);
                List<Requirements> requirementsList = new ArrayList<>();
                requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Wi-Fi"));
                requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Computer"));
                requirementsList.add(new Requirements(R.drawable.ic_menu_camera, " Projector"));

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                requirementsGridView.setLayoutManager(gridLayoutManager);
                requirementsGridView.setAdapter(new RequirementsListAdapter(getApplicationContext(), requirementsList));

                String subjectCode = content.getSubject();
                String subject = CommonCodeUtils.getObjectFromCode(subjectCode).getCodeNameForCurrentLocale();

                String gradeCode = content.getGrade();
                String grade = CommonCodeUtils.getObjectFromCode(gradeCode).getCodeNameForCurrentLocale();

                details.setText(subject +  " | "  + getResources().getString(R.string.grade) + " " + grade);

                loadSimilarTeachingAids();

            } else {
                requirementsLayout.setVisibility(View.GONE);
                String topicCode = content.getTopic();
                String topic = CommonCodeUtils.getObjectFromCode(topicCode).getCodeNameForCurrentLocale();

                String languageCode = content.getLanguage();
                String language = CommonCodeUtils.getObjectFromCode(languageCode).getCodeNameForCurrentLocale();

                details.setText(topic +  " | " + language);

                loadSimilarSelfLearning();
            }

            //TODO : set author name
            authorName.setText("Author ");
            description.setText(content.getInstruction());

            if(getSupportActionBar() != null) {
                Logger.d(" action bar is not null");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(content.getTitle());
            }


        }

    }

    private void loadSimilarTeachingAids() {
        //TODO similar resources - get resources with same file type, language, subject, grade - confirm

        TeachingAidsContentRequest contentRequest = new TeachingAidsContentRequest(UserDetailUtils.getUserId(getApplicationContext()),
                content.getFileType(), content.getLanguage(), content.getSubject(), content.getGrade());
        RestClient.getApiService("").searchTeachingAids(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        contentRecyclerView.setLayoutManager(linearLayoutManager);
                        similarContentsAdapter = new BaseHorizontalCardListAdapter(getApplicationContext(), contents);
                        contentRecyclerView.setAdapter(similarContentsAdapter);


                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Logger.d(" on fail");
            }
        });
    }

    private void loadSimilarSelfLearning() {
        //TODO similar resources - get resources with same file type, language, subject, grade - confirm
        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(UserDetailUtils.getUserId(getApplicationContext()),
                content.getLanguage(), content.getTopic());
        RestClient.getApiService("").searchSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        contentRecyclerView.setLayoutManager(linearLayoutManager);
                        similarContentsAdapter = new BaseHorizontalCardListAdapter(getApplicationContext(), contents);
                        contentRecyclerView.setAdapter(similarContentsAdapter);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            player = youTubePlayer;
            String fileName = content.getFileName();
            String videoID = fileName.substring(fileName.lastIndexOf('/') + 1);
            player.cueVideo(videoID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Logger.d(" error " + youTubeInitializationResult.name());
        if (youTubeInitializationResult.equals(YouTubeInitializationResult.SERVICE_VERSION_UPDATE_REQUIRED)) {
            //handle failure
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player != null) {
            player.release();
        }

        if(similarContentsAdapter != null) {
            similarContentsAdapter.releaseLoaders();
        }
    }
}
