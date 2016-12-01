package net.mavericklabs.mitra.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/11/16.
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

        //Load Video
        if(holder.getItemViewType() == 0) {
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

        holder.videoTitle.setText(getObject(holder).getTitle());

//        if(getObject(holder).getType() == Constants.Type.TEACHING_AIDS) {
//            holder.details.setText("Subject | Grade");
//        } else {
//            holder.details.setText("Topic | Language");
//        }

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
