package send.nutez.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import send.nutez.Activities.HistoryActivity;
import send.nutez.Activities.MealDetailActivity;
import send.nutez.DataAdapters.TableHelper;
import send.nutez.MainActivity;
import send.nutez.R;
import send.nutez.model.Meal;
import send.nutez.model.Person;
import send.nutez.utils.ScoreCalculator;
import send.nutez.utils.StorageDatabaseUtils;

public class DailyFragment extends Fragment {

    public boolean GET_DAILY = true;
    // TODO: set data correctly
    private int waterAmount = 13;
    private int foodScore = 55;
    public int year = 0;
    public int month = 0;
    public int date = 0;
    public String dateSTR = "ES IST DER HEUTIGE TAG";
    public String mealHeaderString = "You ate today";

    // UI Ids
    TextView dateText = null;
    TextView foodText = null;
    TextView waterText = null;
    CircularProgressBar waterBar = null;
    CircularProgressBar foodBar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return (ViewGroup) inflater.inflate(R.layout.daily_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GET_DAILY) {
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
            date = Calendar.getInstance().get(Calendar.DATE);
        }
        setData();
    }

    @Override
    public void onPause() {
        super.onPause();
        // TODO: CLEAR FRAGMENT DATA, when leaving this page
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initIDs();
        // TODO: Check and set initial input data (GET_DAILY)
        setData();
    }

    private class DetailClickListener implements TableDataClickListener<String[]> {
        @Override
        public void onDataClicked(int rowindex, String[] clickedDetail) {
            String dayString = "";
            if (clickedDetail.length > 0)
                dayString = clickedDetail[1];
            //for (String s:clickedDetail)
            //    dayString += s;
            Toast.makeText(getContext(), dayString, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MealDetailActivity.class);
            intent.putExtra("MEAL_ID", dayString);
            getActivity().startActivity(intent);
        }
    }

    private void initIDs() {
        if (!GET_DAILY) {
            getView().findViewById(R.id.imageView3).setVisibility(View.INVISIBLE);
        }
        dateText = getView().findViewById(R.id.dailyDateText);
        foodText = getView().findViewById(R.id.foodAmountText);
        waterText = getView().findViewById(R.id.waterAmountText);
        waterBar = getView().findViewById(R.id.waterProgressBar);
        foodBar = getView().findViewById(R.id.foodProgressBar);
    }

    public void setData() {
        dateSTR = date + "." + (month + 1) + "." + year;
        dateText.setText(dateSTR);
        List<Meal> meals = StorageDatabaseUtils.getMealsForDay(year, month, date);
        String[] mealHeader = { mealHeaderString, "ID" };
        String[][] mealData = TableHelper.mealsToTable(meals);

        Calendar cal = new GregorianCalendar(year, month, date);

        foodScore = ScoreCalculator.getFoodPercentage(cal.getTimeInMillis());
        waterAmount = ScoreCalculator.getWaterPercentage(cal.getTimeInMillis());

        foodText.setText(foodScore + "%");
        waterText.setText(waterAmount + "%");
        waterBar.setProgress(waterAmount);
        foodBar.setProgress(foodScore);
        fillDailyTable(mealHeader, mealData);
        //fillDemoTable();
    }

    // DEMO TABLE DATA
    public static final String[][] DATA_TO_SHOW = { { "This", "is", "a", "test" },
                                                     { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
            { "and", "a", "second", "test" },
    };
    public static final String[] HEADER_TO_SHOW = { "asdfsadfsaf", "2sasdf", "a3", "qwer4"} ;

    public void fillDailyTable(String[] header, String[][] data) {
        TableView<String[]> tv = (TableView<String[]>) getView().findViewById(R.id.historyTable);
        tv.addDataClickListener(new DetailClickListener());
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

    public void fillDemoTable() {
        fillDailyTable(HEADER_TO_SHOW, DATA_TO_SHOW);
    }
}
