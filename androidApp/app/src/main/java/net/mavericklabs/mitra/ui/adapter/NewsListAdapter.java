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
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.News;
import net.mavericklabs.mitra.ui.activity.NewsDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 9/11/16.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CardViewHolder> {

    private Context context;
    private List<News> newsList;

    public NewsListAdapter(Context applicationContext, List<News> newsList) {
        this.context = applicationContext;
        this.newsList = newsList;
    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        holder.title.setText(newsList.get(holder.getAdapterPosition()).getNewsTitle());
        holder.text2.setText(newsList.get(holder.getAdapterPosition()).getContent());
        holder.newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News item = newsList.get(holder.getAdapterPosition());
                Intent details = new Intent(context, NewsDetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("news_item",item);
                details.putExtras(bundle);
                context.startActivity(details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.text2)
        TextView text2;

        @BindView(R.id.card_news)
        CardView newsCard;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
