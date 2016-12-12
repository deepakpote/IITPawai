package net.mavericklabs.mitra.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.database.model.DbNotification;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by amoghpalnitkar on 12/8/16.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private RealmResults<DbNotification> notifications;

    public NotificationAdapter(Context context, RealmResults<DbNotification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.notificationBody.setText(notifications.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notification_body)
        TextView notificationBody;

        NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
