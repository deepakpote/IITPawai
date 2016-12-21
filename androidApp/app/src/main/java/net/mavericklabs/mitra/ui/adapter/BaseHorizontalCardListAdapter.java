package net.mavericklabs.mitra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.activity.ContentDetailsActivity;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;
import net.mavericklabs.mitra.utils.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 9/11/16.
 */

public class BaseHorizontalCardListAdapter extends RecyclerView.Adapter<BaseHorizontalCardListAdapter.CardViewHolder> {

    private Context context;
    private List<Content> contents;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;

    public BaseHorizontalCardListAdapter(Context applicationContext, List<Content> contents) {
        this.context = applicationContext;
        this.contents = contents;
        thumbnailViewToLoaderMap = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {
        if(contents.get(position).getFileType().equals(Constants.FileTypeVideo))
            return 0;

        return 1;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item_card_view,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        ViewGroup.LayoutParams layoutParams = holder.contentView.getLayoutParams();
        layoutParams.width = displayMetrics.widthPixels / 2 - (DisplayUtils.dpToPx(24, context));
        layoutParams.height = layoutParams.width + (DisplayUtils.dpToPx(8, context));
        holder.contentView.setLayoutParams(layoutParams);

        DisplayUtils.displayFileIcon(getObject(holder).getFileType(), holder.fileIcon);

        //Load Video
        if(holder.getItemViewType() == 0) {
            holder.youTubeThumbnailView.setVisibility(View.VISIBLE);
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    holder.contentView.setBackgroundColor(Color.BLACK);

                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                }
            };

            holder.youTubeThumbnailView.initialize(Constants.youtubeDeveloperKey, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    thumbnailViewToLoaderMap.put(youTubeThumbnailView, youTubeThumbnailLoader);
                    String fileName = contents.get(holder.getAdapterPosition()).getFileName();
                    String videoID = StringUtils.getVideoKeyFromUrl(fileName);

                    youTubeThumbnailLoader.setVideo(videoID);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        } else {
            //Show file Icon
            holder.youTubeThumbnailView.setVisibility(View.GONE);
        }

        holder.videoTitle.setText(getObject(holder).getTitle());

        if(getObject(holder).getContentTypeCodeID().equals(Constants.ContentTypeTeachingAids)) {
            Integer subjectCode = contents.get(holder.getAdapterPosition()).getSubject();
            String subject = CommonCodeUtils.getObjectFromCode(subjectCode).getCodeNameForCurrentLocale();

            Integer gradeCode = contents.get(holder.getAdapterPosition()).getGrade();
            String grade = CommonCodeUtils.getObjectFromCode(gradeCode).getCodeNameForCurrentLocale();

            holder.details.setText(subject +  " | "  + context.getResources().getString(R.string.grade) + " " + grade);
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
                bundle.putSerializable("content", (Serializable) contents.get(holder.getAdapterPosition()));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    private Content getObject(RecyclerView.ViewHolder holder) {
        return contents.get(holder.getAdapterPosition());
    }

    public void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.content_thumbnail)
        RelativeLayout contentView;

        @BindView(R.id.youtube_thumbnail)
        YouTubeThumbnailView youTubeThumbnailView;

        @BindView(R.id.content_title)
        TextView videoTitle;

        @BindView(R.id.details)
        TextView details;

        @BindView(R.id.file_icon)
        ImageView fileIcon;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
