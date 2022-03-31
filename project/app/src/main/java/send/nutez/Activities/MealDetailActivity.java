package send.nutez.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import send.nutez.Fragments.DailyFragment;
import send.nutez.R;

public class MealDetailActivity extends AppCompatActivity {

    public String mealDetailHeader = "Meal Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("HEADER_TEXT");
            //The key argument here must match that used in the other activity
            TextView tv = findViewById(R.id.mealDetailTitle);
            tv.setText(value);
        }
        fillDetailTable(DailyFragment.HEADER_TO_SHOW, DailyFragment.DATA_TO_SHOW);
    }

    public void fillDetailTable(String[] header, String[][] data) {
        TableView<String[]> tv = (TableView<String[]>) findViewById(R.id.mealDetailTable);
        SimpleTableDataAdapter sa = new SimpleTableDataAdapter(this, data);
        SimpleTableHeaderAdapter ha = new SimpleTableHeaderAdapter(this, header);
        ha.setTextColor(Color.RED);
        ha.setTextSize(20);
        sa.setTextColor(Color.RED);
        sa.setTextSize(15);
        tv.setColumnCount(header.length);
        tv.setHeaderAdapter(ha);
        tv.setDataAdapter(sa);
    }
}