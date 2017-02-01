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

package net.mavericklabs.mitra.ui.adapter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.ContentDataRequest;
import net.mavericklabs.mitra.model.api.ContentDataResponse;
import net.mavericklabs.mitra.model.api.GenericListDataModel;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.activity.ContentDetailsActivity;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by amoghpalnitkar on 9/11/16.
 */

public class ContentVerticalCardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Content> contents;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private CardViewHolder toDownloadHolder;
    private Fragment callingFragment;
    private boolean showDeleteOption = false;
    private int loaderPosition = -1;
    private static final String PDF_EXTENSION = ".pdf";

    public ContentVerticalCardListAdapter(Context applicationContext, List<Content> contents, Fragment fragment) {
        this.context = applicationContext;
        this.contents = contents;
        this.callingFragment = fragment;
        thumbnailViewToLoaderMap = new HashMap<>();
    }

    public void showLoading() {
        if(loaderPosition < 0) {
            contents.add(new Content());
            loaderPosition = contents.size() - 1;
            Logger.d(" loading " + contents.size());
            notifyDataSetChanged();
        }
    }

    public void stopLoading() {
        Logger.d("stop loading " + loaderPosition);
        if(loaderPosition > 0) {
            contents.remove(loaderPosition);
            loaderPosition = -1;
            notifyDataSetChanged();
        }
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
    public List<Content> getContents() {
        return contents;
    }

    @Override
    public int getItemViewType(int position) {
        if(StringUtils.isEmpty(contents.get(position).getContentID())) {
            //Loader View
            return 2;
        } else if(contents.get(position).getFileType().equals(Constants.FileTypeVideo)){
            //Video View
            return 0;
        }

        //Default View
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType < 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view_vertical,parent,false);
            return new CardViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_loading_panel,parent,false);
            return new LoaderViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if(viewHolder instanceof CardViewHolder) {
            final CardViewHolder holder = (CardViewHolder) viewHolder;

            ViewGroup.LayoutParams layoutParams = holder.contentView.getLayoutParams();
            layoutParams.width = displayMetrics.widthPixels / 3;
            layoutParams.height = layoutParams.width;
            holder.contentView.setLayoutParams(layoutParams);

            if(showDeleteOption) {
                holder.deleteResource.setVisibility(View.VISIBLE);

                holder.deleteResource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog dialog =
                                new AlertDialog.Builder(callingFragment.getActivity())
                                        .setMessage(context.getString(R.string.delete_saved_resource_confirmation))
                                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Content content = contents.get(holder.getAdapterPosition());
                                                removeFromSavedContent(content.getContentID(), holder);
                                            }
                                        })
                                        .setNegativeButton(context.getString(R.string.cancel),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //do nothing
                                                    }
                                                })
                                        .create();
                        dialog.show();

                    }
                });
            } else {
                holder.deleteResource.setVisibility(View.GONE);
            }

            DisplayUtils.displayFileIcon(getObject(holder).getFileType(), holder.fileIcon);
            //Load Video
            if(holder.getItemViewType() == 0) {
                holder.saveButton.setVisibility(View.GONE);
                loadContent(holder);
            } else if(holder.getItemViewType() == 1) {

                holder.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toDownloadHolder = holder;
                        if(checkPermission()) {
                            downloadContent(holder);
                        }
                    }
                });

                //Show file Icon
                holder.youTubeThumbnailView.setVisibility(View.GONE);
                loadContent(holder);
            }
        } else if(viewHolder instanceof LoaderViewHolder) {
            LoaderViewHolder holder = (LoaderViewHolder) viewHolder;

            holder.loadingPanel.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = holder.loadingPanel.getLayoutParams();
            layoutParams.height = DisplayUtils.dpToPx(80, context);
            holder.loadingPanel.setLayoutParams(layoutParams);
        }
    }

    private void loadContent(final CardViewHolder holder) {
        holder.videoTitle.setText(contents.get(holder.getAdapterPosition()).getTitle());
        if(getObject(holder).getContentTypeCodeID().equals(Constants.ContentTypeTeachingAids)) {
            Integer subjectCode = contents.get(holder.getAdapterPosition()).getSubject();
            String subject = CommonCodeUtils.getObjectFromCode(subjectCode).getCodeNameForCurrentLocale();

            List<Integer> gradeCodes = StringUtils.splitCommas(contents.get(holder.getAdapterPosition()).getGrade());
            List<String> gradeNames = new ArrayList<>();
            for (Integer gradeCode : gradeCodes) {
                gradeNames.add(CommonCodeUtils.getObjectFromCode(gradeCode).getCodeNameForCurrentLocale());
            }
            String grades = StringUtils.stringify(gradeNames);
            holder.details.setText(subject +  " | "  + context.getResources().getString(R.string.grade) + " " + grades);
        } else {

            Integer topicCode = contents.get(holder.getAdapterPosition()).getTopic();
            String topic = CommonCodeUtils.getObjectFromCode(topicCode).getCodeNameForCurrentLocale();

            Integer languageCode = contents.get(holder.getAdapterPosition()).getLanguage();
            String language = CommonCodeUtils.getObjectFromCode(languageCode).getCodeNameForCurrentLocale();

            holder.details.setText(topic +  " | " + language);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ContentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("content", (Serializable) getObject(holder));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);

        if(viewHolder instanceof CardViewHolder) {
            final CardViewHolder holder = (CardViewHolder) viewHolder;
            if(holder.getItemViewType() == 0) {
                holder.youTubeThumbnailView.setVisibility(View.GONE);

                if(!thumbnailViewToLoaderMap.containsKey(holder.youTubeThumbnailView)) {
                    //Ensure that thumbnail view is not init
                    holder.youTubeThumbnailView.initialize(Constants.youtubeDeveloperKey, new YouTubeThumbnailView.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                            thumbnailViewToLoaderMap.put(holder.youTubeThumbnailView, youTubeThumbnailLoader);
                            loadThumbnail(youTubeThumbnailLoader, holder);
                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
                } else {
                    YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(holder.youTubeThumbnailView);
                    loadThumbnail(loader, holder);
                }
            }
        }

    }

    private void loadThumbnail(final YouTubeThumbnailLoader youTubeThumbnailLoader, final CardViewHolder holder) {

        Content content = contents.get(holder.getAdapterPosition());
        if(content != null) {
            String fileName = content.getFileName();
            String videoID = StringUtils.getVideoKeyFromUrl(fileName);
            youTubeThumbnailLoader.setVideo(videoID);
            youTubeThumbnailLoader.
                    setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    holder.youTubeThumbnailView.setVisibility(View.VISIBLE);
                    holder.contentView.setBackgroundColor(Color.BLACK);
                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                }
            });
        }

    }

    private void removeFromSavedContent(String contentID, final CardViewHolder holder) {
        String token = UserDetailUtils.getToken(context);
        RestClient.getApiService(token).saveContent(contentID ,false)
                .enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                    @Override
                    public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                        if(response.isSuccessful()) {
                            Logger.d("content saved..");
                            contents.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        } else {
                            Logger.d("is saved not success");
                            Toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                        Logger.d("is saved onfailure");
                        Toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            callingFragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return false;
        }

        return true;
    }

    private Content getObject(RecyclerView.ViewHolder holder) {
        return contents.get(holder.getAdapterPosition());
    }

    public void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Logger.d(" on permission result");
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadContent(toDownloadHolder);

                } else {

                }
                return;
            }
        }
    }

    private void downloadContent(final CardViewHolder holder) {
        Logger.d(" download ");
        Content content = contents.get(holder.getAdapterPosition());
        String token = UserDetailUtils.getToken(context);
        Call<BaseModel<ContentDataResponse>> saveRequest = RestClient.getApiService(token)
                .download(new ContentDataRequest(content.getContentID()));

        saveRequest.enqueue(new Callback<BaseModel<ContentDataResponse>>() {
            @Override
            public void onResponse(Call<BaseModel<ContentDataResponse>> call, Response<BaseModel<ContentDataResponse>> response) {
                if(response.isSuccessful()) {
                    List<ContentDataResponse> responseList = response.body().getData();
                    Logger.d(" file " + responseList.get(0).getFileName());

                    final Content content = contents.get(holder.getAdapterPosition());

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
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            if(response.isSuccessful()) {
                                String downloadFileName = mitraDirectoryPath +
                                        File.separator + content.getTitle() + PDF_EXTENSION;
                                File downloadedFile = new File(downloadFileName);
                                BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                                sink.writeAll(response.body().source());
                                sink.close();
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, context.getString(R.string.download_complete),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    Toast.makeText(context, context.getString(R.string.download_file_location,
                            mitraDirectoryPath + File.separator +content.getTitle()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ContentDataResponse>> call, Throwable t) {
                Logger.d(" on failure ");
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public boolean showDeleteOption() {
        return showDeleteOption;
    }

    public void setShowDeleteOption(boolean showDeleteOption) {
        this.showDeleteOption = showDeleteOption;
    }

    class LoaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.loading_panel_layout)
        RelativeLayout loadingPanel;

        public LoaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content_thumbnail)
        RelativeLayout contentView;

        @BindView(R.id.youtube_thumbnail)
        YouTubeThumbnailView youTubeThumbnailView;

        @BindView(R.id.content_title)
        TextView videoTitle;

        @BindView(R.id.file_icon)
        ImageView fileIcon;

        @BindView(R.id.details)
        TextView details;

        @BindView(R.id.save_button)
        TextView saveButton;

        @BindView(R.id.delete_resource)
        ImageView deleteResource;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
