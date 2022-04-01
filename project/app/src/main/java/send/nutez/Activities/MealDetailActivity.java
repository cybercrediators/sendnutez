package send.nutez.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import send.nutez.DataAdapters.TableHelper;
import send.nutez.Fragments.DailyFragment;
import send.nutez.MainActivity;
import send.nutez.R;
import send.nutez.model.Meal;
import send.nutez.utils.ScoreCalculator;
import send.nutez.utils.StorageDatabaseUtils;

public class MealDetailActivity extends AppCompatActivity {

    public String mealDetailHeader = "Meal Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("HEADER_TEXT");
            String mealID = extras.getString("MEAL_ID");
            Long id = Long.parseLong(mealID);
            Meal m = StorageDatabaseUtils.getMealByID(id);
            String[] mealHeader = { "Ingredients", "Information" };
            String[][] mealContent = TableHelper.singleMealToTable(m);

            String[] detailHeader = { "Nutrient", "Score" };
            String[][] detailContent = ScoreCalculator.getMealDetailTable(m);
            //The key argument here must match that used in the other activity
            mealDetailHeader = m.getName();
            TextView tv = findViewById(R.id.mealDetailTitle);
            tv.setText(mealDetailHeader);
            TableView<String[]> md = (TableView<String[]>) findViewById(R.id.mealDetailTable);
            TableView<String[]> ingDet = (TableView<String[]>) findViewById(R.id.ingredientsDetailTable);
            fillDetailTable(mealHeader, mealContent, ingDet);
            fillDetailTable(detailHeader, detailContent, md);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void fillDetailTable(String[] header, String[][] data, TableView<String[]> tv) {
        SimpleTableDataAdapter sa = new SimpleTableDataAdapter(this, data);
        SimpleTableHeaderAdapter ha = new SimpleTableHeaderAdapter(this, header);
        ha.setTextColor(Color.LTGRAY);
        ha.setTextSize(20);
        sa.setTextColor(Color.LTGRAY);
        sa.setTextSize(15);
        tv.setColumnCount(header.length);
        tv.setHeaderAdapter(ha);
        tv.setDataAdapter(sa);
    }
}