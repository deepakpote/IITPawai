package net.mavericklabs.mitra.ui.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.listener.OnMonthSelectedListener;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.LanguageUtils;
import net.mavericklabs.mitra.utils.Logger;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amoghpalnitkar on 15/11/16.
 */

public class CalendarView extends RelativeLayout {

    @BindView(R.id.calendar_topbar)
    RelativeLayout topBarLayout;

    @BindView(R.id.calendar_days_of_week)
    LinearLayout daysOfWeekLayout;

    @BindView(R.id.calendar_grid)
    GridView datesGrid;

    @BindView(R.id.month_year_selector)
    TextView monthYearSelector;

    @BindView(R.id.count_trainings_this_month)
    TextView trainingsCount;

    @BindView(R.id.drop_down_image_view)
    ImageView dropDownImageView;

    //days of the week times number of rows to show
    private final int DAYS_COUNT = 7 * 5;
    private Calendar currentDate = Calendar.getInstance();
    private int currentSelectedMonth = currentDate.get(Calendar.MONTH);
    private int currentSelectedYear = currentDate.get(Calendar.YEAR);
    private CalendarAdapter adapter;
    private OnMonthSelectedListener onMonthSelectedListener;


    public void setTrainingsCount(int trainingsCount) {
        this.trainingsCount.setText(getResources().getQuantityString(R.plurals.training_count, trainingsCount, trainingsCount));
    }

    public HashSet<Date> getEventDates() {
        return eventDates;
    }

    public void setEventDates(HashSet<Date> eventDates) {
        this.eventDates = eventDates;
    }

    private HashSet<Date> eventDates;

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

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String currentMonth = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)];
        if(LanguageUtils.getCurrentLanguage() == Constants.AppLanguageEnglish) {
            monthYearSelector.setText(currentMonth.substring(0,3) + " " + calendar.get(Calendar.YEAR));
        } else {
            monthYearSelector.setText(currentMonth + " " + calendar.get(Calendar.YEAR));
        }

        monthYearSelector.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        dropDownImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        updateCalendar();
    }

    public GridView getDatesGrid() {
        return datesGrid;
    }

    public CalendarAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CalendarAdapter adapter) {
        this.adapter = adapter;
    }

    public void updateCalendar() {
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
        adapter = new CalendarAdapter(getContext(),cells,eventDates);
        datesGrid.setAdapter(adapter);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener listener) {
        this.onMonthSelectedListener = listener;
    }

    private void showDatePickerDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = layoutInflater.inflate(R.layout.dialog_date_picker,null);
        final DatePicker datePicker = (DatePicker) dialogLayout.findViewById(R.id.date_picker);
        datePicker.updateDate(currentSelectedYear, currentSelectedMonth, 1);
        Button doneButton = (Button) dialogLayout.findViewById(R.id.done_button);

        String locale;
        //In N and above(?) the date picker follows the locale of the app.
        //Below N, the date picker follows the locale of the system.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Locale.getDefault().getCountry();
        } else {
            locale = Resources.getSystem().getConfiguration().locale.getCountry();
        }
        Logger.d(" country system default " + locale);
        if(locale.equals("US")) {
            //Date picker is of format: Month - Day - Year. Hide day (1)
            ((ViewGroup)((ViewGroup)(datePicker.getChildAt(0))).getChildAt(0)).getChildAt(1).setVisibility(GONE);
        } else {
            //Date picker is of format: Day - Month - Year. Hide day (0)
            ((ViewGroup)((ViewGroup)(datePicker.getChildAt(0))).getChildAt(0)).getChildAt(0).setVisibility(GONE);
        }

        final AlertDialog datePickerDialog = new AlertDialog.Builder(getContext())
                .setView(dialogLayout)
                .create();
        datePickerDialog.show();
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectedMonth = datePicker.getMonth();
                currentSelectedYear = datePicker.getYear();
                currentDate.set(currentSelectedYear,currentSelectedMonth,1);
                String monthName = new DateFormatSymbols().getMonths()[currentSelectedMonth];
                if(LanguageUtils.getCurrentLanguage() == Constants.AppLanguageEnglish) {
                    String substring = monthName.substring(0,3);
                    monthYearSelector.setText(substring + " " + currentSelectedYear);
                } else {
                    monthYearSelector.setText(monthName + " " + currentSelectedYear);
                }

                datePickerDialog.dismiss();
                updateCalendar();
                if(onMonthSelectedListener != null) {
                    onMonthSelectedListener.onMonthSelected(currentDate.get(Calendar.MONTH), currentDate.get(Calendar.YEAR));
                }
            }
        });
    }

    public boolean isAnEventDay(Date date, HashSet<Date> eventDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        for (Date eventDate : eventDays) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(eventDate);
            int eventYear = calendar1.get(Calendar.YEAR);
            int eventMonth = calendar1.get(Calendar.MONTH);
            int eventDay = calendar1.get(Calendar.DAY_OF_MONTH);

//            Logger.d( " event day  " + eventDay + " current " + day);
//            Logger.d( " event month  " + eventMonth + " current " + month);
//            Logger.d( " event yr  " + eventYear + " current " + year);
            if (eventDay == day && eventMonth == month && eventYear == year) {
                return true;
            }
        }

        return false;
    }

    public class CalendarAdapter extends BaseAdapter {
        // days with events
        private HashSet<Date> eventDays;

        private ArrayList<Date> calendarDates;

        // for view inflation
        private LayoutInflater inflater;

        CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays) {
            this.eventDays = eventDays;
            this.calendarDates = days;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return calendarDates.size();
        }

        @Override
        public Date getItem(int i) {
            return calendarDates.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent)
        {
            // day in question
            Date date = getItem(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            // today
            Date today = new Date();
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(today);

            // inflate item if it does not exist yet
            if (view == null) {
                view = inflater.inflate(R.layout.custom_calendar_date_item, parent, false);
            }

            // if this day has an event, specify event image
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            ImageView eventBackground = (ImageView) view.findViewById(R.id.event_background);

            if (eventDays != null) {
                if(isAnEventDay(date, eventDays)) {
                    eventBackground.setImageResource(R.drawable.background_training);
                }
            }

            // clear styling
            dateText.setTypeface(null, Typeface.NORMAL);
            dateText.setTextColor(Color.BLACK);

            if (month != currentSelectedMonth || year != currentSelectedYear) {
                // if this day is outside current month, grey it out
                dateText.setTextColor(getResources().getColor(R.color.default_grey));
            } else if (day == todayCalendar.get(Calendar.DAY_OF_MONTH)
                    && month == todayCalendar.get(Calendar.MONTH)
                    && year == todayCalendar.get(Calendar.YEAR)) {
                // if it is today, set it to accent/bold
                dateText.setTypeface(null, Typeface.BOLD);
                dateText.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            // set text
            dateText.setText(String.valueOf(calendar.get(Calendar.DATE)));

            return view;
        }
    }


}
