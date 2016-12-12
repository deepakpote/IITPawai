package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.database.model.DbNotification;
import net.mavericklabs.mitra.ui.adapter.NotificationAdapter;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by amoghpalnitkar on 12/8/16.
 */

public class NotificationFragment extends Fragment {

    @BindView(R.id.notification_recycler_view)
    RecyclerView notificationRecyclerView;

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
        RealmResults<DbNotification> notifications = realm.where(DbNotification.class).findAll();
        Logger.d("notification size : " + notifications.size());
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecyclerView.setAdapter(new NotificationAdapter(getContext(),notifications));
    }
}
