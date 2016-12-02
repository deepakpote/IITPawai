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
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 9/11/16.
 */

public class ContentVerticalCardListAdapter extends RecyclerView.Adapter<ContentVerticalCardListAdapter.CardViewHolder> {

    private Context context;
    private List<Content> contents;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;

    public ContentVerticalCardListAdapter(Context applicationContext, List<Content> contents) {
        this.context = applicationContext;
        this.contents = contents;
        thumbnailViewToLoaderMap = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {
        return contents.get(position).getFileType().ordinal();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_card_view,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        ViewGroup.LayoutParams layoutParams = holder.contentView.getLayoutParams();
        layoutParams.width = displayMetrics.widthPixels / 3;
        layoutParams.height = layoutParams.width - (DisplayUtils.dpToPx(16, context));
        holder.contentView.setLayoutParams(layoutParams);


        //Load Video
        if(holder.getItemViewType() == Constants.FileType.VIDEO.ordinal()) {
            holder.youTubeThumbnailView.setVisibility(View.VISIBLE);
            holder.fileIcon.setVisibility(View.GONE);
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
                    youTubeThumbnailLoader.setVideo("AZ2ZPmEfjvU");
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        } else {
            //Show file Icon

            holder.fileIcon.setVisibility(View.VISIBLE);
            holder.youTubeThumbnailView.setVisibility(View.GONE);
        }

        holder.videoTitle.setText(contents.get(holder.getAdapterPosition()).getTitle());
        if(getObject(holder).getType() == Constants.Type.TEACHING_AIDS) {
            holder.details.setText("Subject | Grade");
        } else {
            holder.details.setText("Topic | Language");
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

        @BindView(R.id.file_icon)
        ImageView fileIcon;

        @BindView(R.id.details)
        TextView details;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
