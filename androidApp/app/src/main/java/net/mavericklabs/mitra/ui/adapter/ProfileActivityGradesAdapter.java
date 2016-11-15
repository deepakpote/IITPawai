package net.mavericklabs.mitra.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.ui.adapter.viewholder.ChipViewHolder;

/**
 * Created by root on 11/11/16.
 */

public class ProfileActivityGradesAdapter extends RecyclerView.Adapter<ChipViewHolder> {
    @Override
    public ChipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip,parent,false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChipViewHolder holder, int position) {
        holder.subjectNameTextView.setText("Grade 1");
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
