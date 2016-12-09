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

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.Event;
import net.mavericklabs.mitra.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                title.setText(event.getSummary());
                description.setText(event.getDescription());

                String date = event.getStartTime().getDateTime();
                Date convertedDate = DateUtils.convertToDate(date, "yyyy-MM-dd'T'HH:mm:ssXXX");
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
