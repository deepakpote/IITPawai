package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.model.api.EventRequest;
import net.mavericklabs.mitra.listener.OnMonthSelectedListener;
import net.mavericklabs.mitra.model.Event;
import net.mavericklabs.mitra.ui.custom.CalendarView;
import net.mavericklabs.mitra.utils.DateUtils;
import net.mavericklabs.mitra.utils.Logger;

import java.net.ConnectException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amoghpalnitkar on 15/11/16.
 */

public class EventCalendarFragment extends Fragment implements OnMonthSelectedListener{

    @BindView(R.id.calendar_view)
    CalendarView calendarView;

    @BindView(R.id.loading_panel)
    RelativeLayout loadingPanel;

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

        Calendar calendar = Calendar.getInstance();

        DateUtils.setTimeToBeginningOfMonth(calendar);
        Logger.d(" begin " + calendar.getTime());

        String startTime = DateUtils.convertToServerFormatFromDate(calendar.getTime());

        DateUtils.setTimeToEndOfMonth(calendar);
        Logger.d(" end " + calendar.getTime());

        String endTime = DateUtils.convertToServerFormatFromDate(calendar.getTime());
        loadEvents(startTime, endTime);

    }


    private void loadEvents(String timeMin, String timeMax) {
        final HashMap<Date, Event> dateEventHashMap = new HashMap<>();
        final Calendar calendar = Calendar.getInstance();
        loadingPanel.setVisibility(View.VISIBLE);
        EventRequest contentRequest = new EventRequest(timeMin, timeMax, "startTime");
        RestClient.getApiService("").listEvents(contentRequest).enqueue(new Callback<BaseModel<Event>>() {
            @Override
            public void onResponse(Call<BaseModel<Event>> call, Response<BaseModel<Event>> response) {
                Logger.d(" Succes");
                loadingPanel.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {
                        //set event dates here
                        final HashSet<Date> eventDates = new HashSet<>();

                        final List<Event> events = response.body().getData();
                        for (Event event  : events) {
                            String startTime = event.getStartTime().getDateTime();
                            Date date = DateUtils.convertToDate(startTime, "yyyy-MM-dd'T'HH:mm:ssZ");

                            calendar.setTime(date);
                            DateUtils.setTimeToBeginningOfDay(calendar);

                            eventDates.add(calendar.getTime());
                            dateEventHashMap.put(calendar.getTime(), event);
                            Logger.d(" Add date " + calendar.getTime());
                        }

                        calendarView.setEventDates(eventDates);
                        calendarView.setTrainingsCount(eventDates.size());
                        calendarView.updateCalendar();

                        calendarView.setOnMonthSelectedListener(EventCalendarFragment.this);

                        calendarView.getDatesGrid().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Date clickedDate = calendarView.getAdapter().getItem(i);
                                calendar.setTime(clickedDate);
                                DateUtils.setTimeToBeginningOfDay(calendar);
                                clickedDate = calendar.getTime();
                                Logger.d("date " + clickedDate);

                                if(dateEventHashMap.containsKey(clickedDate)) {
                                    Logger.d(" contains ");
                                    Event event = dateEventHashMap.get(clickedDate);
                                    BottomSheetDialogFragment bottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("event", event);
                                    bottomSheetDialogFragment.setArguments(bundle);
                                    bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
                                }

                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Event>> call, Throwable t) {
                Logger.d(" on fail");
                if(t instanceof ConnectException) {
                    Toast.makeText(getContext(), getString(R.string.error_check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMonthSelected(int month, int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        DateUtils.setTimeToBeginningOfMonth(calendar);
        Logger.d(" begin " + calendar.getTime());

        String startTime = DateUtils.convertToServerFormatFromDate(calendar.getTime());

        DateUtils.setTimeToEndOfMonth(calendar);
        Logger.d(" end " + calendar.getTime());

        String endTime = DateUtils.convertToServerFormatFromDate(calendar.getTime());
        loadEvents(startTime, endTime);

    }
}
