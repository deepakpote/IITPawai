package net.mavericklabs.mitra.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DisplayUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 9/11/16.
 */

public class BaseHorizontalVideoCardListAdapter extends RecyclerView.Adapter<BaseHorizontalVideoCardListAdapter.CardViewHolder> {

    private Context context;
    private List<Content> contents;

    public BaseHorizontalVideoCardListAdapter(Context applicationContext, List<Content> contents) {
        this.context = applicationContext;
        this.contents = contents;
    }

    @Override
    public int getItemViewType(int position) {
        return contents.get(position).getFileType().ordinal();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view,parent,false);
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
        if(holder.getItemViewType() == Constants.FileType.VIDEO.ordinal()) {
            holder.youTubeThumbnailView.setVisibility(View.VISIBLE);
            holder.fileIcon.setVisibility(View.GONE);
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

                }

                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                }
            };

            holder.youTubeThumbnailView.initialize(Constants.youtubeDeveloperKey, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo("IVpOyKCNZYw");
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

        @BindView(R.id.youtube_video_title)
        TextView videoTitle;

        @BindView(R.id.file_icon)
        ImageView fileIcon;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
