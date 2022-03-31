package send.nutez.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import send.nutez.R;

public class HistoryDetailFragment extends Fragment {

    private TableView<String[]> tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return (ViewGroup) inflater.inflate(R.layout.history_detail_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tv = (TableView<String[]>) getView().findViewById(R.id.historyTable);
        fillHistoryDetails(DailyFragment.HEADER_TO_SHOW, DailyFragment.DATA_TO_SHOW);
        tv.addDataClickListener(new HistoryClickListener());
    }

    private class HistoryClickListener implements TableDataClickListener<String[]> {
        @Override
        public void onDataClicked(int rowindex, String[] clickedDay) {
            String dayString = "";
            for (String s:clickedDay)
                dayString += s;
            Toast.makeText(getContext(), dayString, Toast.LENGTH_SHORT).show();
            DailyFragment f = new DailyFragment();
            f.dateSTR = "NICHT HEUTE";
            f.GET_DAILY = false;
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.historyContainer, f)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void fillHistoryDetails(String[] header, String[][] data) {
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
}
