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

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.ContentDataRequest;
import net.mavericklabs.mitra.model.api.ContentDataResponse;
import net.mavericklabs.mitra.model.api.MetaContent;
import net.mavericklabs.mitra.model.api.GenericListDataModel;
import net.mavericklabs.mitra.model.api.LikeRequest;
import net.mavericklabs.mitra.model.api.SelfLearningContentRequest;
import net.mavericklabs.mitra.model.api.TeachingAidsContentRequest;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.Requirements;
import net.mavericklabs.mitra.ui.adapter.BaseHorizontalCardListAdapter;
import net.mavericklabs.mitra.ui.adapter.RequirementsListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentDetailsActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener {

    @BindView(R.id.similar_contents_recycler_view) RecyclerView contentRecyclerView;
    @BindView(R.id.content_card_view) CardView contentCardView;
    @BindView(R.id.content_image_view) ImageView contentImageView;
    @BindView(R.id.requirements_grid_view) RecyclerView requirementsGridView;
    @BindView(R.id.content_title) TextView title;
    @BindView(R.id.author_name) TextView authorName;
    @BindView(R.id.details) TextView details;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.requirements_layout) LinearLayout requirementsLayout;
    @BindView(R.id.loading_panel) RelativeLayout loadingPanel;
    @BindView(R.id.like_icon) ImageView likeIcon;
    @BindView(R.id.save_icon) ImageView saveIcon;
    @BindView(R.id.download_icon) ImageView downloadIcon;
    @BindView(R.id.share_icon) ImageView shareIcon;
    @BindView(R.id.youtube_layout) RelativeLayout youTubeLayout;
    @BindView(R.id.content_layout) RelativeLayout contentLayout;
    @BindView(R.id.content_web_view) WebView contentWebView;
    @BindView(R.id.loading_panel_for_web_view) RelativeLayout loadingPanelForWebView;

    private static final int EXTERNAL_STORE_WRITE_REQUEST_CODE = 1;

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

                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            content.setSaved(isSaved);
                            realm.copyToRealmOrUpdate(content);
                            realm.commitTransaction();


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

    @OnClick(R.id.download_icon)
    void downloadContent() {
        Logger.d(" download ");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ContentDetailsActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ContentDetailsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORE_WRITE_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        } else {
            downloadPdf();
        }
    }

    private void downloadPdf() {
        String token = UserDetailUtils.getToken(getApplicationContext());
        Call<BaseModel<ContentDataResponse>> saveRequest = RestClient.getApiService(token)
                .download(new ContentDataRequest(content.getContentID()));

        saveRequest.enqueue(new Callback<BaseModel<ContentDataResponse>>() {
            @Override
            public void onResponse(Call<BaseModel<ContentDataResponse>> call, Response<BaseModel<ContentDataResponse>> response) {
                if(response.isSuccessful()) {
                    List<ContentDataResponse> responseList = response.body().getData();
                    Logger.d(" file " + responseList.get(0).getFileName());

                    String url = responseList.get(0).getFileName();
                    // get download service and enqueue file
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Environment.getExternalStorageDirectory());
                    stringBuilder.append(File.separator);
                    stringBuilder.append("MITRA");
                    final String mitraDirectoryPath = stringBuilder.toString();
                    File mitraDirectory = new File(mitraDirectoryPath);
                    Logger.d("Directory Path " + mitraDirectoryPath);
                    mitraDirectory.mkdirs();
                    final OkHttpClient client = new OkHttpClient();
                    try {
                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        final String extension;
                        if(content.getFileType() == Constants.FileTypeAudio) {
                            extension = ".mp3";
                        } else {
                            extension = ".pdf";
                        }
                        client.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                if(response.isSuccessful()) {
                                    String downloadFileName = mitraDirectoryPath +
                                            File.separator + content.getTitle() + extension;
                                    File downloadedFile = new File(downloadFileName);
                                    BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                                    sink.writeAll(response.body().source());
                                    sink.close();
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), getString(R.string.download_complete),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                        Toast.makeText(getApplicationContext(), getString(R.string.download_file_location,
                                mitraDirectoryPath + File.separator +content.getTitle()),
                                Toast.LENGTH_LONG).show();
                    }catch (IllegalArgumentException ex) {
                        Logger.d(" error " + ex.getMessage());
                        Toast.makeText(ContentDetailsActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<BaseModel<ContentDataResponse>> call, Throwable t) {
                Logger.d(" on failure ");
            }
        });
    }

    BaseHorizontalCardListAdapter similarContentsAdapter;
    private Content content;
    private YouTubePlayer player;
    private boolean isLiked;
    private boolean isSaved;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        ViewGroup.LayoutParams imageLayoutParams = contentImageView.getLayoutParams();
        imageLayoutParams.height = displayMetrics.heightPixels / 3;
        contentImageView.setLayoutParams(imageLayoutParams);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            content = (Content) bundle.getParcelable("content");
        }

        if(content != null) {

            Bundle firebaseBundle = new Bundle();
            firebaseBundle.putString(FirebaseAnalytics.Param.ITEM_ID, content.getTitle());
            firebaseBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, content.getTitle());
            firebaseBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CommonCodeUtils.getObjectFromCode(content.getContentTypeCodeID()).getCodeNameEnglish());
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, firebaseBundle);


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
                contentWebView.setVisibility(View.GONE);
                contentLayout.setBackgroundColor(Color.BLACK);
                downloadIcon.setVisibility(View.GONE);
                YouTubePlayerSupportFragment frag =
                        (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
                frag.initialize(Constants.youtubeDeveloperKey, this);

            } else if (content.getFileType().equals(Constants.FileTypePPT) ||
                    content.getFileType().equals(Constants.FileTypeWorksheet) ||
                    content.getFileType().equals(Constants.FileTypePDF)) {
                youTubeLayout.setVisibility(View.GONE);
                contentImageView.setVisibility(View.GONE);
                ViewGroup.LayoutParams webViewLayoutParams = contentWebView.getLayoutParams();
                webViewLayoutParams.height = (int)(displayMetrics.heightPixels / 2.62);
                contentWebView.setLayoutParams(webViewLayoutParams);
                contentWebView.getSettings().setJavaScriptEnabled(true);
                contentWebView.getSettings().setDomStorageEnabled(true);
                contentWebView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        loadingPanelForWebView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadResource(WebView view, String url) {
                        super.onLoadResource(view, url);
                        Logger.d("on load resource..");
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Logger.d("on page finished..");
                        loadingPanelForWebView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Logger.d("error : " + error.toString());
                        loadingPanelForWebView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                                    WebResourceResponse errorResponse) {
                        super.onReceivedHttpError(view, request, errorResponse);
                        Logger.d("received http error : ");
                    }
                });
                Logger.d("file path " + content.getFileName());
                contentWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + content.getFileName());

                contentWebView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content.getFileName()));
                            startActivity(browserIntent);
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                //Show file Icon
                contentImageView.setVisibility(View.VISIBLE);
                youTubeLayout.setVisibility(View.GONE);
                contentWebView.setVisibility(View.GONE);
                contentLayout.setBackgroundResource(R.drawable.gradient_background);
                contentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                String requirements = content.getRequirement();

                if(!StringUtils.isEmpty(requirements)) {
                    requirementsLayout.setVisibility(View.VISIBLE);

                    String[] list = requirements.split(",");

                    List<Requirements> requirementsList = new ArrayList<>();
                    for (String requirement : list) {
                        try {
                            Integer requirementCodeID = Integer.valueOf(requirement);
                            CommonCode requirementObject = CommonCodeUtils.getObjectFromCode(requirementCodeID);
                            requirementsList.add(new Requirements(R.drawable.ic_important_devices_black_18dp,
                                    requirementObject.getCodeNameForCurrentLocale()));
                        } catch (NumberFormatException ex) {
                            Logger.d(" " + ex.getMessage());
                        }

                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    requirementsGridView.setLayoutManager(gridLayoutManager);
                    requirementsGridView.setAdapter(new RequirementsListAdapter(getApplicationContext(), requirementsList));

                }

                Integer subjectCode = content.getSubject();
                String subject = CommonCodeUtils.getObjectFromCode(subjectCode).getCodeNameForCurrentLocale();

                List<Integer> gradeCodes = StringUtils.splitCommas(content.getGrade());
                List<String> gradeNames = new ArrayList<>();
                for (Integer gradeCode : gradeCodes) {
                    gradeNames.add(CommonCodeUtils.getObjectFromCode(gradeCode).getCodeNameForCurrentLocale());
                }
                String grades = StringUtils.stringify(gradeNames);
                details.setText(subject +  " | "  + getResources().getString(R.string.grade) + " " + grades);

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
                content.getGrade());
        String token = UserDetailUtils.getToken(getApplicationContext());
        RestClient.getApiService(token).searchTeachingAids(LanguageUtils.getCurrentLanguage(), contentRequest).enqueue(new Callback<BaseModel<Content>>() {
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
        RestClient.getApiService(token).searchSelfLearning(LanguageUtils.getCurrentLanguage(), contentRequest).enqueue(new Callback<BaseModel<Content>>() {
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

            //handle failure
            AlertDialog alertDialog = new AlertDialog.Builder(ContentDetailsActivity.this)
                    .setMessage("The Youtube Player is not working. Please try updating your YouTube App and try again.")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Uri uri = Uri.parse("market://details?id=com.google.android.youtube");
                            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myAppLinkToMarket);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getApplicationContext(), " Unable to find market app", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Not Now", null)
                    .create();

            alertDialog.show();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == EXTERNAL_STORE_WRITE_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPdf();
            } else {
                Toast.makeText(this, "Permission denied. Unable to download.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
