package net.mavericklabs.mitra.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.Api;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.database.DbNotification;
import net.mavericklabs.mitra.ui.activity.NewsDetailsActivity;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void onBindViewHolder(final NotificationViewHolder holder, final int position) {
        holder.notificationBody.setText(notifications.get(position).getBody());
        holder.notificationTitle.setText(notifications.get(position).getTitle());
        holder.notificationTime.setText(DateUtils.timeFormat(notifications.get(position).getReceivedTime(),"MMM dd"));
        if(!StringUtils.isEmpty(notifications.get(position).getObjectID())) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = holder.getAdapterPosition();
                    fetchContentDetails(notifications.get(currentPosition));
                }
            });
        }
    }

    private void fetchContentDetails(DbNotification notification) {
        //TODO determine notification type and fetch news || content accordingly
        if(notification.getNotificationTypeId().equals(Constants.NotificationTypeNews)) {
            Intent intent = new Intent(context,NewsDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("news_item",notification.getObjectID());
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        //TODO get content details from content id
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notification_title)
        TextView notificationTitle;

        @BindView(R.id.notification_body)
        TextView notificationBody;

        @BindView(R.id.notification_time)
        TextView notificationTime;

        @BindView(R.id.notification_layout)
        LinearLayout layout;

        NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
