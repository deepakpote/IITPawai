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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Requirements;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 9/11/16.
 */

public class RequirementsListAdapter extends RecyclerView.Adapter<RequirementsListAdapter.CardViewHolder> {

    private Context context;
    private List<Requirements> requirementsList;

    public RequirementsListAdapter(Context applicationContext, List<Requirements> requirementsList) {
        this.context = applicationContext;
        this.requirementsList = requirementsList;
    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classroom_requirements,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {

        holder.title.setText(requirementsList.get(holder.getAdapterPosition()).getTitle());
        holder.index.setText(holder.getAdapterPosition() + 1 + ".");

    }

    @Override
    public int getItemCount() {
        return requirementsList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.requirement_text)
        TextView title;

        @BindView(R.id.requirement_index)
        TextView index;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
