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

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.ContentDataRequest;
import net.mavericklabs.mitra.api.model.ContentDataResponse;
import net.mavericklabs.mitra.api.model.MetaContent;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.LikeRequest;
import net.mavericklabs.mitra.api.model.SelfLearningContentRequest;
import net.mavericklabs.mitra.api.model.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Requirements;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.RequirementsListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.content_title)
    TextView title;

    @BindView(R.id.author_name)
    TextView authorName;

    @BindView(R.id.details)
    TextView details;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.requirements_layout)
    LinearLayout requirementsLayout;

    @BindView(R.id.loading_panel)
    RelativeLayout loadingPanel;

    @BindView(R.id.like_icon)
    ImageView likeIcon;

    @BindView(R.id.save_icon)
    ImageView saveIcon;

    @BindView(R.id.share_icon)
    ImageView shareIcon;

    @BindView(R.id.youtube_layout)
    RelativeLayout youTubeLayout;

    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;

    @OnClick(R.id.share_icon)
    void shareContent() {
        Logger.d(" share ");
        String token = UserDetailUtils.getToken(getApplicationContext());
        Call<BaseModel<ContentDataResponse>> saveRequest = RestClient.getApiService(token)
                .share(new ContentDataRequest(content.getContentID()));

        saveRequest.enqueue(new Callback<BaseModel<ContentDataResponse>>() {
            @Override
            public void onResponse(Call<BaseModel<ContentDataResponse>> call, Response<BaseModel<ContentDataResponse>> response) {
                if(response.isSuccessful()) {
                    List<ContentDataResponse> responseList = response.body().getData();
                    Logger.d(" file " + responseList.get(0).getFileName());

                    String shareBody = getString(R.string.message_share) + responseList.get(0).getFileName();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MITRA " + content.getTitle());
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_title)));
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ContentDataResponse>> call, Throwable t) {
                Logger.d(" on failure ");
            }
        });
    }

    @OnClick(R.id.like_icon)
    void likeContent() {
        if(isLiked) {
            likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_grey_24dp));
            isLiked = false;
        } else {
            likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_accent_24dp));
            isLiked = true;
        }
        String token = UserDetailUtils.getToken(getApplicationContext());
        RestClient.getApiService(token).likeContent(new LikeRequest(content.getContentID(),isLiked))
                .enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                    @Override
                    public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                        if(response.isSuccessful()) {
                            Logger.d("content liked..");
                        } else {
                            Logger.d("is liked " + isLiked);
                            if(isLiked) {
                                likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_grey_24dp));
                                isLiked = false;
                            } else {
                                likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_accent_24dp));
                                isLiked = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                        Logger.d("is liked " + isLiked);
                        if(isLiked) {
                            likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_grey_24dp));
                            isLiked = false;
                        } else {
                            likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_accent_24dp));
                            isLiked = true;
                        }
                    }
                });
    }

    @OnClick(R.id.save_icon)
    void saveContent() {
        if(isSaved) {
            saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_lightgrey_24dp));
            isSaved = false;
        } else {
            saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_accent_24dp));
            isSaved = true;
        }
        String token = UserDetailUtils.getToken(getApplicationContext());
        RestClient.getApiService(token).saveContent(content.getContentID(),isSaved)
                .enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                    @Override
                    public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                        if(response.isSuccessful()) {
                            Logger.d("content saved..");
                        } else {
                            Logger.d("is liked " + isSaved);
                            if(isSaved) {
                                saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_lightgrey_24dp));
                                isSaved = false;
                            } else {
                                saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_accent_24dp));
                                isSaved = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                        Logger.d("is liked " + isLiked);
                        if(isSaved) {
                            saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_lightgrey_24dp));
                            isSaved = false;
                        } else {
                            saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_accent_24dp));
                            isSaved = true;
                        }
                    }
                });
    }

    BaseHorizontalCardListAdapter similarContentsAdapter;
    private Content content;
    private YouTubePlayer player;
    private boolean isLiked;
    private boolean isSaved;

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

            String token = UserDetailUtils.getToken(getApplicationContext());
            RestClient.getApiService(token).metaContent(content.getContentID())
                    .enqueue(new Callback<BaseModel<MetaContent>>() {
                            @Override
                            public void onResponse(Call<BaseModel<MetaContent>> call, Response<BaseModel<MetaContent>> response) {
                                if(response.isSuccessful()) {
                                    isLiked = response.body().getData().get(0).isLiked();
                                    isSaved = response.body().getData().get(0).isSaved();
                                    if(isSaved) {
                                        saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_accent_24dp));
                                    } else {
                                        saveIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_lightgrey_24dp));
                                    }

                                    if(isLiked) {
                                        likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_accent_24dp));
                                    } else {
                                        likeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_grey_24dp));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseModel<MetaContent>> call, Throwable t) {

                            }
                        });

            DisplayUtils.displayFileIcon(content.getFileType(), contentImageView);

            //Load Video
            if(content.getFileType().equals(Constants.FileTypeVideo)) {

                youTubeLayout.setVisibility(View.VISIBLE);
                contentImageView.setVisibility(View.GONE);
                contentLayout.setBackgroundColor(Color.BLACK);
                YouTubePlayerSupportFragment frag =
                        (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
                frag.initialize(Constants.youtubeDeveloperKey, this);

            } else {
                //Show file Icon
                contentImageView.setVisibility(View.VISIBLE);
                youTubeLayout.setVisibility(View.GONE);
                contentLayout.setBackgroundResource(R.drawable.gradient_background);
                contentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(content.getFileType().equals(Constants.FileTypePPT) ||
                                content.getFileType().equals(Constants.FileTypeWorksheet) ||
                                content.getFileType().equals(Constants.FileTypePDF)) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content.getFileName()));
                            startActivity(browserIntent);
                        }

                        if(content.getFileType().equals(Constants.FileTypeAudio)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(content.getFileName()), "audio/mp3");
                            startActivity(intent);
                        }
                    }
                });
            }

            if(content.getContentTypeCodeID().equals(Constants.ContentTypeTeachingAids)) {

                requirementsLayout.setVisibility(View.VISIBLE);

                String requirements = content.getRequirement();

                String[] list = requirements.split(",");

                List<Requirements> requirementsList = new ArrayList<>();
                for (String requirement : list) {
                    //TODO : this wil be used if we use actual icons for requirement
                    requirementsList.add(new Requirements(R.drawable.ic_add_accent_18dp, requirement));
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                requirementsGridView.setLayoutManager(gridLayoutManager);
                requirementsGridView.setAdapter(new RequirementsListAdapter(getApplicationContext(), requirementsList));

                Integer subjectCode = content.getSubject();
                String subject = CommonCodeUtils.getObjectFromCode(subjectCode).getCodeNameForCurrentLocale();

                Integer gradeCode = content.getGrade();
                String grade = CommonCodeUtils.getObjectFromCode(gradeCode).getCodeNameForCurrentLocale();

                details.setText(subject +  " | "  + getResources().getString(R.string.grade) + " " + grade);

                loadSimilarTeachingAids();

            } else {
                requirementsLayout.setVisibility(View.GONE);
                Integer topicCode = content.getTopic();
                String topic = CommonCodeUtils.getObjectFromCode(topicCode).getCodeNameForCurrentLocale();

                Integer languageCode = content.getLanguage();
                String language = CommonCodeUtils.getObjectFromCode(languageCode).getCodeNameForCurrentLocale();

                details.setText(topic +  " | " + language);

                loadSimilarSelfLearning();
            }

            title.setText(content.getTitle());
            authorName.setText(content.getAuthor());
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


        TeachingAidsContentRequest contentRequest = new TeachingAidsContentRequest(content.getFileType(),
                content.getSubject().toString(),
                content.getGrade().toString());
        String token = UserDetailUtils.getToken(getApplicationContext());
        RestClient.getApiService(token).searchTeachingAids(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                Logger.d(" Succes");
                loadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();
                        Content contentToRemove = null;

                        //Remove the content that is being shown
                        for (Content similarContent : contents) {
                            if(similarContent.getContentID().equals(content.getContentID())){
                                contentToRemove = similarContent;
                                break;
                            }
                        }
                        if(contentToRemove != null) {
                            contents.remove(contentToRemove);

                        }

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

        SelfLearningContentRequest contentRequest = new SelfLearningContentRequest(content.getLanguage().toString(),
                content.getTopic().toString());
        String token = UserDetailUtils.getToken(getApplicationContext());
        RestClient.getApiService(token).searchSelfLearning(contentRequest).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                loadingPanel.setVisibility(View.GONE);
                Logger.d(" Succes");
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        List<Content> contents = response.body().getData();

                        Content contentToRemove = null;

                        //Remove the content that is being shown
                        for (Content similarContent : contents) {
                            if(similarContent.getContentID().equals(content.getContentID())){
                                contentToRemove = similarContent;
                                break;
                            }
                        }
                        if(contentToRemove != null) {
                            contents.remove(contentToRemove);

                        }

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
            Logger.d("file name is : " + fileName);
            String videoID = StringUtils.getVideoKeyFromUrl(fileName);
            Logger.d("video id is : " + videoID);
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
