package send.nutez.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import send.nutez.Activities.PredictionEdit;
import send.nutez.MainActivity;
import send.nutez.R;

public class SlidingFragment extends Fragment {
    private int waterAmount = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return (ViewGroup) inflater.inflate(R.layout.main_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button wButton = getView().findViewById(R.id.addWaterButton);
        if (wButton != null)
            wButton.setOnClickListener(addWaterListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        Button waterButton = getView().findViewById(R.id.addWaterButton);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int waterAmount = Integer.parseInt(sharedPreferences.getString("water_amount", "100"));
        waterButton.setText("+" + waterAmount + " (ml) Water");
    }

    View.OnClickListener addWaterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.addWater(waterAmount);
            Toast.makeText(getContext(),"added water", Toast.LENGTH_LONG).show();
        }
    };

}
