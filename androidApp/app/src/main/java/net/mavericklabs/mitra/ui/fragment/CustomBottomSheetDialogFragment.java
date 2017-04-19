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

package net.mavericklabs.mitra.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.Attend;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.GenericListDataModel;
import net.mavericklabs.mitra.model.Event;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.net.ConnectException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishakha on 08/12/16.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.training_title)
    TextView title;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.date)
    TextView dateTextView;

    @BindView(R.id.location)
    TextView location;

    private String eventID, eventName;

    @OnClick(R.id.attend_button)
    void attend() {
        String token = UserDetailUtils.getToken(getContext());
        RestClient.getApiService(token).attendEvent(new Attend(eventID))
                .enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                    @Override
                    public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                        if(response.isSuccessful()) {
                            Logger.d("attend event..");
                            Toast.makeText(getContext(), getString(R.string.toast_attend, eventName), Toast.LENGTH_LONG).show();
                        } else {
                            Logger.d(" not success");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                        Logger.d("on failure");
                        if(t instanceof ConnectException) {
                            Toast.makeText(getContext(), getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public CustomBottomSheetDialogFragment() {
        super();
    }

    private BottomSheetBehavior.BottomSheetCallback bottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, dialog);

        Event event;
        Bundle bundle = getArguments();
        if(bundle != null) {
            event = (Event) bundle.getSerializable("event");
            if(event != null) {
                eventID = event.getId();
                eventName = event.getSummary();
                title.setText(event.getSummary());
                description.setText(event.getDescription());

                String date = event.getStartTime().getDateTime();
                Date convertedDate = DateUtils.convertToDate(date, "yyyy-MM-dd'T'HH:mm:ssZ");
                String newDate = DateUtils.convertToString(convertedDate, "EEE, MMM d, yy");

                dateTextView.setText(newDate);
                location.setText(event.getLocation());
            }
        }



        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetBehaviorCallback);
        }
    }
}
