package net.mavericklabs.mitra.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.Api;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.database.DbNotification;
import net.mavericklabs.mitra.ui.activity.ContentDetailsActivity;
import net.mavericklabs.mitra.ui.activity.NewsDetailsActivity;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.LanguageUtils;
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
    private FragmentActivity activity;

    public NotificationAdapter(Context context, RealmResults<DbNotification> notifications, FragmentActivity activity) {
        this.context = context;
        this.notifications = notifications;
        this.activity = activity;
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
        Logger.d("object id " + notifications.get(position).getObjectID());
        Logger.d("body : " + notifications.get(position).getBody());
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
        } else if(notification.getNotificationTypeId().equals(Constants.NotificationTypeSelfLearning) ||
                    notification.getNotificationTypeId().equals(Constants.NotificationTypeTeachingAids)) {
            fetchContent(notification.getObjectID());
        }

        //TODO get content details from content id
    }

    private void fetchContent(final String contentId) {
        String token = UserDetailUtils.getToken(context);
        int languageCodeId = LanguageUtils.getCurrentLanguage();
        Api api = RestClient.getApiService(token);
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(context.getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
        api.getContent(contentId,languageCodeId).enqueue(new Callback<BaseModel<Content>>() {
            @Override
            public void onResponse(Call<BaseModel<Content>> call, Response<BaseModel<Content>> response) {
                dialog.dismiss();
                if(response.isSuccessful()) {
                    Content content = response.body().getData().get(0);
                    if(content != null) {
                        Intent intent = new Intent(context,ContentDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("content",content);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } else {
                    Toast.makeText(context,context.getString(R.string.error_message),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Content>> call, Throwable t) {
                Toast.makeText(context,context.getString(R.string.error_check_internet),Toast.LENGTH_SHORT).show();
            }
        });
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
