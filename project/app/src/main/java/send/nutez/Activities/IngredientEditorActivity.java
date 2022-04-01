package send.nutez.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Map;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import send.nutez.DataAdapters.IngredientAdapter;
import send.nutez.MainActivity;
import send.nutez.R;
import send.nutez.model.Meal;
import send.nutez.utils.DataGetterThingy;

public class IngredientEditorActivity extends AppCompatActivity {

    private String filepath;
    private HashMap<String, List<Float>> predictions;

    private EditText searchFoodField;
    private EditText namefield;
    private ListView listView;
    private Button searchButton;
    private Button saveButton;
    private Button discardButton;
    private String mealName;

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
        searchButton.setOnClickListener(addFoodButton);
        saveButton.setOnClickListener(saveMealButton);
        discardButton.setOnClickListener(discardListener);
    }

    private void initIDs() {
        searchButton = findViewById(R.id.searchFoodButton);
        namefield = findViewById(R.id.mealNamefield);
        saveButton = findViewById(R.id.saveMealButton);
        listView = findViewById(R.id.listView);
        discardButton = findViewById(R.id.discardMealButton);
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

    View.OnClickListener discardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    View.OnClickListener addFoodButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Do you really want to add " + searchFoodField.getText() + "?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    };

    View.OnClickListener saveMealButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e("Save", "save food");
            if (namefield != null)
                mealName = namefield.getText().toString();
            addMealToDatabase(mealName);
            switchBackToMain();
        }
    };

    private void addMealToDatabase(String mealName) {
        Map<String, Float> recipe = new HashMap<>();
        for (int i=0;i<adapter.getCount();i++){
            //Log.e("OUT", ((HashMap<String, String>)adapter.getItem(i)).get("value") + " " + ((HashMap<String, String>)adapter.getItem(i)).get("name"));
            HashMap<String, String> val = (HashMap<String, String>) adapter.getItem(i);
            recipe.put(val.get("name"), Float.parseFloat(val.get("value")));
        }
        DataGetterThingy.getMeal(recipe, new Continuation<Meal>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                if (o instanceof Meal) {
                    Meal meal = (Meal) o;
                    Toast.makeText(getApplicationContext(), "added meal " + meal.getName(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Meal could not be added!", Toast.LENGTH_LONG).show();
                }
            }
        }, mealName);
    }

    private void switchBackToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addElementToScrollView() {
        String value = searchFoodField.getText().toString();
        if (value.isEmpty())
            return;
        HashMap<String, String> elem = new HashMap<String, String>();
        elem.put("name", value);
        elem.put("value", "0.5");
        data.add(elem);
        adapter.notifyDataSetChanged();
    }

    // Ask if you want to add the found food
    // YES/NO dialog
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    addElementToScrollView();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

}