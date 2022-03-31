package send.nutez.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import send.nutez.R;

public class DailyFragment extends Fragment {

    public static boolean GET_DAILY = true;
    // TODO: set data correctly
    private int waterAmount = 13;
    private int foodScore = 55;
    private String dateSTR = "ES IST DER HEUTIGE TAG";

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initIDs();
        // TODO: Check and set initial input data (GET_DAILY)
        setData();
    }

    private void initIDs() {
        dateText = getView().findViewById(R.id.dailyDateText);
        foodText = getView().findViewById(R.id.foodAmountText);
        waterText = getView().findViewById(R.id.waterAmountText);
        waterBar = getView().findViewById(R.id.waterProgressBar);
        foodBar = getView().findViewById(R.id.foodProgressBar);
    }

    public void setData() {
        dateText.setText(dateSTR);
        foodText.setText(foodScore + "%");
        waterText.setText(waterAmount + "%");
        waterBar.setProgress(waterAmount);
        foodBar.setProgress(foodScore);
        fillDemoTable();
    }

    // DEMO TABLE DATA
    private static final String[][] DATA_TO_SHOW = { { "This", "is", "a", "test" },
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
    private static final String[] HEADER_TO_SHOW = { "asdfsadfsaf", "2sasdf", "a3", "qwer4"} ;

    public void fillDailyTable(String[] header, String[][] data) {
        TableView<String[]> tv = (TableView<String[]>) getView().findViewById(R.id.dailyTableView);
        SimpleTableDataAdapter sa = new SimpleTableDataAdapter(getContext(), data);
        SimpleTableHeaderAdapter ha = new SimpleTableHeaderAdapter(getContext(), header);
        ha.setTextColor(Color.RED);
        ha.setTextSize(20);
        sa.setTextColor(Color.RED);
        sa.setTextSize(15);
        tv.setColumnCount(header.length);
        tv.setHeaderAdapter(ha);
        tv.setDataAdapter(sa);
    }

    public void fillDemoTable() {
        fillDailyTable(HEADER_TO_SHOW, DATA_TO_SHOW);
    }
}
