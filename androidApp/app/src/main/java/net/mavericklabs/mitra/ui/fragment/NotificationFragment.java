package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.database.DbNotification;
import net.mavericklabs.mitra.ui.adapter.NotificationAdapter;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by amoghpalnitkar on 12/8/16.
 */

public class NotificationFragment extends Fragment {

    @BindView(R.id.notification_recycler_view)
    RecyclerView notificationRecyclerView;

    @BindView(R.id.no_notifications_text)
    TextView noNotificationsText;

    public NotificationFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DbNotification> notifications = realm.where(DbNotification.class).findAllSorted("receivedTime", Sort.DESCENDING);
        Logger.d("notification size : " + notifications.size());
        if(notifications.isEmpty()) {
            notificationRecyclerView.setVisibility(View.GONE);
            noNotificationsText.setVisibility(View.VISIBLE);
        }
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecyclerView.setAdapter(new NotificationAdapter(getContext(),notifications));
    }
}
