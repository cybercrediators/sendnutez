package send.nutez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import send.nutez.Fragments.DailyDetailViewFragment;
import send.nutez.model.Nute;
import send.nutez.utils.NuteDatabaseUtils;
import send.nutez.utils.StorageDatabaseUtils;

import send.nutez.Fragments.CameraFragment;
import send.nutez.Fragments.DailyFragment;
import send.nutez.Fragments.SlidingFragment;

public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_STRING = "LALALA";
    private static final int PAGES = 4;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Important! Don't to shit with data above this line
        NuteDatabaseUtils.initialize(getApplicationContext());

        for (Nute n: StorageDatabaseUtils.getAllNutes()) {
            Log.d(DEBUG_STRING, Integer.toString(StorageDatabaseUtils.getNuteReferenceValueByName(n.getName()).size()));
        }
        /*test();

        for(Meal m : StorageDatabaseUtils.getAllMeals()) {
            StringBuilder s = new StringBuilder();
            for(Ingredient i : m.getIngredients()) {
                s.append(i.getName() + " ");
            }
            Log.d(DEBUG_STRING, m.getName() + ": " + s.toString());
        }*/
        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new SliderPageAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1, false);
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
                    return new DailyFragment();
                case 3:
                    DailyDetailViewFragment frag = new DailyDetailViewFragment();
                    return frag;
                default:
                    return new SlidingFragment();
            }
        }

        @Override
        public int getItemCount() {
            return PAGES;
        }
    }

}
