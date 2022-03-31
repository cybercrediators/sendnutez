package send.nutez.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import send.nutez.DataAdapters.IngredientAdapter;
import send.nutez.MainActivity;
import send.nutez.R;

public class IngredientEditorActivity extends AppCompatActivity {

    private String filepath;
    private HashMap<String, List<Float>> predictions;

    private TextView detectedFoodString;
    private EditText searchFoodField;
    private ListView listView;
    private Button searchButton;
    private Button saveButton;

    //private ArrayList<String> data;
    private ArrayList<HashMap<String, String>> data;
    //private ArrayAdapter<String> adapter;
    private IngredientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_editor);

        // get selected image
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filepath = extras.getString("IMG_PATH");
            predictions = (HashMap<String, List<Float>>) extras.getSerializable("PRED_LIST");
        }

        initIDs();
        initListeners();
    }

    private void initListeners() {
        detectedFoodString.setText(predictions.keySet().toString());
        searchButton.setOnClickListener(addFoodButton);
        saveButton.setOnClickListener(saveMealButton);
    }

    private void initIDs() {
        detectedFoodString = findViewById(R.id.detectedFoodString);
        searchButton = findViewById(R.id.searchFoodButton);
        saveButton = findViewById(R.id.saveMealButton);
        listView = findViewById(R.id.listView);
        searchFoodField = findViewById(R.id.searchFoodField);
        // Init array adapter
        //data = new ArrayList<String>();
        data = new ArrayList<HashMap<String, String>>();
        if (!predictions.isEmpty()) {
            for (String p : predictions.keySet()) {
                HashMap<String, String> elem = new HashMap<String, String>();
                elem.put("name", p);
                elem.put("value", "0.0");
                data.add(elem);
            }
        }
        //adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.ingredient_element_layout, R.id.ingreedText, data);
        adapter = new IngredientAdapter(this, data);
        listView.setAdapter(adapter);
    }

    View.OnClickListener addFoodButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String value = searchFoodField.getText().toString();
            if (value.isEmpty())
                return;
            HashMap<String, String> elem = new HashMap<String, String>();
            elem.put("name", value);
            elem.put("value", "0.5");
            data.add(elem);
            adapter.notifyDataSetChanged();
        }
    };

    View.OnClickListener saveMealButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e("Save", "save food");
            String allValues="";
            ArrayList<String> valueList = new ArrayList<String>();
            for (int i=0;i<adapter.getCount();i++){
                //Log.e("OUT", ((HashMap<String, String>)adapter.getItem(i)).get("value") + " " + ((HashMap<String, String>)adapter.getItem(i)).get("name"));
                allValues +=((HashMap<String,String>)adapter.getItem(i)).get("value")+ ",";
                valueList.add(((HashMap<String,String>)adapter.getItem(i)).get("value"));
            }
            // use this valueList as per ur requirement
            allValues = allValues.substring(0,allValues.length()-1);
            Toast.makeText(IngredientEditorActivity.this, allValues, Toast.LENGTH_LONG).show();
        }
    };

    private void addElementToScrollView(String name) {
    }

    // Ask if you want to add the found food
    // YES/NO dialog
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

}