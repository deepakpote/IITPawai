package net.mavericklabs.mitra.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.ui.adapter.viewholder.ChipViewHolder;
import net.mavericklabs.mitra.utils.DisplayUtils;

import java.util.List;

/**
 * Created by amoghpalnitkar on 11/11/16.
 */

public class ChipLayoutAdapter extends RecyclerView.Adapter<ChipViewHolder> {

    private List<BaseObject> objects;
    private boolean showRemoveButton;
    private OnChipRemovedListener listener;
    private int width;

    public List<BaseObject> getObjects() {
        return objects;
    }

    public void setObjects(List<BaseObject> objects) {
        this.objects = objects;
    }

    public ChipLayoutAdapter(List<BaseObject> objects, Context context) {
        this.objects = objects;
        width = DisplayUtils.dpToPx(120, context);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public ChipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip,parent,false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChipViewHolder holder, int position) {
        holder.subjectNameTextView.setText(objects.get(position).getCommonCode().getCodeNameForCurrentLocale());
        if(showRemoveButton) {
            holder.removeChip.setVisibility(View.VISIBLE);

            holder.removeChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChipRemoved(holder.getAdapterPosition());
                }
            });
        } else {
            holder.removeChip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public boolean isShowRemoveButton() {
        return showRemoveButton;
    }

    public void setShowRemoveButton(boolean showRemoveButton) {
        this.showRemoveButton = showRemoveButton;
    }

    public OnChipRemovedListener getListener() {
        return listener;
    }

    public void setListener(OnChipRemovedListener listener) {
        this.listener = listener;
    }
}
