package net.mavericklabs.mitra.ui.fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.ui.activity.HomeActivity;
import net.mavericklabs.mitra.ui.custom.CalendarView;
import net.mavericklabs.mitra.utils.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 15/11/16.
 */

public class EventCalendarFragment extends Fragment{

    @BindView(R.id.calendar_view)
    CalendarView calendarView;

    public EventCalendarFragment() {
        super();
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_calendar,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        //set event dates here
        final HashSet<Date> eventDates = new HashSet<>();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 5);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.YEAR, 2016);
        eventDates.add(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, 20);
        eventDates.add(calendar.getTime());
        calendarView.setEventDates(eventDates);
        calendarView.updateCalendar();

        calendarView.getDatesGrid().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Date clickedDate = calendarView.getAdapter().getItem(i);
                Logger.d("date " + clickedDate);

                if(calendarView.isAnEventDay(clickedDate, eventDates)) {
                    BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
                    bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
                }


            }
        });
    }
}
