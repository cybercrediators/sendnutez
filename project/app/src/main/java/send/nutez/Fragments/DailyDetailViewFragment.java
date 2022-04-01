package send.nutez.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import send.nutez.R;
import send.nutez.utils.ScoreCalculator;

public class DailyDetailViewFragment extends Fragment {

    public String dailyDetailHeaderText = "Daily Details";
    private TextView detailHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return (ViewGroup) inflater.inflate(R.layout.daily_detail_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initIDs();
        String[] header = { "Nutrient" , "Score" };
        String[][] content = ScoreCalculator.getDayDetailTable(System.currentTimeMillis());
        fillDetailTable(header, content);
    }

    private void initIDs() {
        detailHeader = getView().findViewById(R.id.detailDailyTitle);
        detailHeader.setText(dailyDetailHeaderText);
    }

    public void fillDetailTable(String[] header, String[][] data) {
        TableView<String[]> tv = (TableView<String[]>) getView().findViewById(R.id.mealDetailTable);
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
