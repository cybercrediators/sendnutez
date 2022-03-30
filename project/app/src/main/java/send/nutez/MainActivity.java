package send.nutez;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import send.nutez.model.Ingredient;
import send.nutez.model.Meal;
import send.nutez.model.Nute;
import send.nutez.utils.NuteDatabaseUtils;
import send.nutez.utils.StorageDatabaseUtils;

public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_STRING = "LALALA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Important! Don't to shit with data above this line
        NuteDatabaseUtils.initialize(getApplicationContext());

        for (Nute n: StorageDatabaseUtils.getAllNutes()) {
            Log.d(DEBUG_STRING, Integer.toString(StorageDatabaseUtils.getNuteReferenceValueByName(n.getName()).size()));
        }

        test();
    }

    private void test() {
        Meal pizza = new Meal("pizza");
        Ingredient cheese = new Ingredient("cheese", 500);
        cheese.setUnit("g");

        Ingredient flour = new Ingredient("flour", 1000);
        flour.setUnit("g");

        Ingredient oil = new Ingredient("olive oil", 100);
        oil.setUnit("ml");

        StorageDatabaseUtils.insert(pizza);

        pizza.addIngredient(cheese);
        pizza.addIngredient(flour);
        pizza.addIngredient(oil);

    }
}