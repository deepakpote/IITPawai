package net.mavericklabs.mitra.ui.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.utils.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 15/11/16.
 */

public class CalendarView extends RelativeLayout{

    @BindView(R.id.calendar_topbar)
    RelativeLayout topBarLayout;

    @BindView(R.id.calendar_days_of_week)
    LinearLayout daysOfWeekLayout;

    @BindView(R.id.calendar_grid)
    GridView datesGrid;

    @BindView(R.id.month_year_spinner)
    Spinner monthYearSpinner;

    //days of the week times number of rows to show
    private final int DAYS_COUNT = 7 * 5;
    private Calendar currentDate = Calendar.getInstance();

    public CalendarView(Context context) {
        super(context);
        initialize(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_calendar,this);
        ButterKnife.bind(this,view);

        String[] choices = {"Nov","Dec"};
        monthYearSpinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,choices));
        currentDate.setTime(new Date());
        updateCalendar();
    }

    private void updateCalendar() {
        Calendar calendar = (Calendar) currentDate.clone();
        ArrayList<Date> cells = new ArrayList<>();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        datesGrid.setAdapter(new CalendarAdapter(getContext(),cells,null));
    }

    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private HashSet<Date> eventDays;

        // for view inflation
        private LayoutInflater inflater;

        CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
            super(context, android.R.layout.simple_list_item_1, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent)
        {
            // day in question
            Date date = getItem(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Logger.d("date is : " + calendar.get(Calendar.DATE));
            int day = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            // today
            Date today = new Date();
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(today);

            // inflate item if it does not exist yet
            if (view == null) {
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year) {
                        // mark this day for event
                        view.setBackgroundResource(R.drawable.background_training);
                        break;
                    }
                }
            }

            // clear styling
            ((TextView)view).setTypeface(null, Typeface.NORMAL);
            ((TextView)view).setTextColor(Color.BLACK);

            if (month != todayCalendar.get(Calendar.MONTH) || year != todayCalendar.get(Calendar.YEAR)) {
                // if this day is outside current month, grey it out
                ((TextView)view).setTextColor(getResources().getColor(R.color.default_grey));
            } else if (day == todayCalendar.get(Calendar.DATE)) {
                // if it is today, set it to blue/bold
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                ((TextView)view).setTextColor(getResources().getColor(R.color.colorAccent));
            }

            // set text
            ((TextView)view).setText(String.valueOf(calendar.get(Calendar.DATE)));

            return view;
        }
    }


}
