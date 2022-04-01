package send.nutez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import send.nutez.model.Ingredient;
import send.nutez.model.IngredientNuteValue;
import send.nutez.model.Meal;
import send.nutez.utils.DataGetterThingy;
import send.nutez.utils.NuteDatabaseUtils;
import send.nutez.utils.PermissionHandler;
import send.nutez.utils.ScoreCalculator;
import send.nutez.utils.StorageDatabaseUtils;

import send.nutez.Fragments.CameraFragment;
import send.nutez.Fragments.MenuFragment;
import send.nutez.Fragments.SlidingFragment;

public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_STRING = "LALALA";
    private static final int PAGES = 3;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionHandler.initialize(this);

        //Important! Don't to shit with data above this line
        NuteDatabaseUtils.initialize(getApplicationContext());

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new SliderPageAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1, false);


        addWater();
        test2();
    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1) {
            super.onBackPressed();
        } else {
            if (viewPager.getCurrentItem() == 0)
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            else
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private class SliderPageAdapter extends FragmentStateAdapter {
        public SliderPageAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CameraFragment();
                case 2:
                    return new MenuFragment();
                default:
                    return new SlidingFragment();
            }
        }

        @Override
        public int getItemCount() {
            return PAGES;
        }
    }

    private void test2() {
        Map<String, Float> recipe = new HashMap<>();
        recipe.put("Apples, fuji, with skin, raw", 100.0f);
        recipe.put("Banana", 200.0f);

        DataGetterThingy.getMeal(recipe, new Continuation<Meal>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                //DO stuff in view
                if (o instanceof Meal) {
                    Meal meal = (Meal) o;
                    Toast.makeText(getApplicationContext(), "added meal " + meal.getName(), Toast.LENGTH_LONG).show();

                    Log.d(DEBUG_STRING, meal.getName());
                    for (Ingredient i : meal.getIngredients()) {
                        Log.d(DEBUG_STRING, "\t" + i.getName());
                        Log.d(DEBUG_STRING, "\t" + i.getInformations());
                        for (IngredientNuteValue val : i.getNutrients()) {
                            Log.d(DEBUG_STRING, "\t\t" + val.getNute().getName() + " " + val.getValue());
                        }
                    }
                    Log.d(DEBUG_STRING, Integer.toString(StorageDatabaseUtils.getMealsForDay(2022, 2, 31).size()));
                    for (Meal m : StorageDatabaseUtils.getAllMeals()) {
                        Log.d(DEBUG_STRING, m.getCreationDate().toString());
                    }


                    double val = ScoreCalculator.getFoodPercentage(System.currentTimeMillis());
                    double wal = ScoreCalculator.getWaterPercentage(System.currentTimeMillis());
                    Log.d(DEBUG_STRING, "percentage" + String.valueOf(val));
                    Log.d(DEBUG_STRING, "percentage" + String.valueOf(wal));
                    String[][] str = ScoreCalculator.getDayDetailTable(System.currentTimeMillis());
                    String[][] str2 = ScoreCalculator.getMealDetailTable(meal);

                    for(int i = 0; i < str.length; i++) {
                        Log.d(DEBUG_STRING, str[i][0] + " " + str[i][1]);
                    }
                    for(int i = 0; i < str2.length; i++) {
                        Log.d(DEBUG_STRING, str[i][0] + " " + str[i][1]);
                    }
                }
            }
        });

    }

    private void addWater() {
        DataGetterThingy.addWater(100, new Continuation<Boolean>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                if(o instanceof Boolean) {
                    if((Boolean) o) {
                        //DO stuff in view
                        Toast.makeText(getApplicationContext(),"added water", Toast.LENGTH_LONG).show();
                        Log.d(DEBUG_STRING, "added water");
                    }
                }
            }
        });
    }
}
