package net.mavericklabs.mitra.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.mavericklabs.mitra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 11/11/16.
 */

public class ChipViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.subject_name_text_view)
    public TextView subjectNameTextView;

    @BindView(R.id.remove_chip)
    public ImageView removeChip;

    public ChipViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
