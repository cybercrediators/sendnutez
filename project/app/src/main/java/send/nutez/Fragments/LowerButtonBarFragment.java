package send.nutez.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import send.nutez.Activities.HistoryActivity;
import send.nutez.Activities.SettingsActivity;
import send.nutez.MainActivity;
import send.nutez.R;

public class LowerButtonBarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return (ViewGroup) inflater.inflate(R.layout.lower_button_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        Button homeButton = getView().findViewById(R.id.homeButton);
        Button settingsButton = getView().findViewById(R.id.settingsButton);
        Button historyButton = getView().findViewById(R.id.historyButton);

        homeButton.setOnClickListener(homeListener);
        settingsButton.setOnClickListener(settingsListener);
        historyButton.setOnClickListener(historyListener);

        if (this.getActivity() instanceof MainActivity) {
            homeButton.setEnabled(false);
        }
        if (this.getActivity() instanceof SettingsActivity) {
            settingsButton.setEnabled(false);
        }
        if (this.getActivity() instanceof HistoryActivity) {
            historyButton.setText("Daily");
            historyButton.setEnabled(false);
        }
    }

    View.OnClickListener historyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            getActivity().startActivity(intent);
        }
    };

    View.OnClickListener settingsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            getActivity().startActivity(intent);
        }
    };

    View.OnClickListener homeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
        }
    };
}
