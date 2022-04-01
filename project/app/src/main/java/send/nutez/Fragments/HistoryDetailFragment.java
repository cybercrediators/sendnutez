package send.nutez.Fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import send.nutez.DataAdapters.TableHelper;
import send.nutez.R;

public class HistoryDetailFragment extends Fragment {

    private TableView<String[]> tv;
    private final static Date START_DATE = new Date(1640991600000l);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return (ViewGroup) inflater.inflate(R.layout.history_detail_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tv = (TableView<String[]>) getView().findViewById(R.id.historyTable);

        // get Days between app creation
        Date curDay = new Date(System.currentTimeMillis());
        Log.e("QWER", curDay.toString());
        List<Date> dates = getDaysBetweenDates(START_DATE, curDay);
        Log.e("DATES", dates.toString());
        Log.e("DATES2", START_DATE.toString());
        String[] historyHeader = { "Dates" };
        String[][] historyContent = TableHelper.datesToTable(dates);
        fillHistoryDetails(historyHeader, historyContent);

        tv.addDataClickListener(new HistoryClickListener());
    }

    private class HistoryClickListener implements TableDataClickListener<String[]> {
        @Override
        public void onDataClicked(int rowindex, String[] clickedDay) {
            String dayString = "";
            if (clickedDay.length > 0)
                dayString = clickedDay[0].toString();
            Toast.makeText(getContext(), dayString, Toast.LENGTH_SHORT).show();
            DailyFragment f = new DailyFragment();
            f.dateSTR = dayString;
            List<String> date = new ArrayList<String>(Arrays.asList(dayString.split("-")));
            if (date.size() == 3) {
                f.year = Integer.parseInt(date.get(0));
                f.month = Integer.parseInt(date.get(1))-1;
                f.date = Integer.parseInt(date.get(2));
            }
            f.mealHeaderString = "You ate on " + dayString;
            f.GET_DAILY = false;
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.historyContainer, f)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
    {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<LocalDate> getDatesBetweenUsingJava8(
            LocalDate startDate, LocalDate endDate) {

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween)
                .mapToObj(i -> startDate.plusDays(i))
                .collect(Collectors.toList());
    }

    public void fillHistoryDetails(String[] header, String[][] data) {
        SimpleTableDataAdapter sa = new SimpleTableDataAdapter(getContext(), data);
        SimpleTableHeaderAdapter ha = new SimpleTableHeaderAdapter(getContext(), header);
        ha.setTextColor(Color.LTGRAY);
        ha.setTextSize(20);
        sa.setTextColor(Color.LTGRAY);
        sa.setTextSize(15);
        tv.setColumnCount(header.length);
        tv.setHeaderAdapter(ha);
        tv.setDataAdapter(sa);
    }
}
