package send.nutez.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import send.nutez.Fragments.SettingsFragment;
import send.nutez.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingsContainer, new SettingsFragment())
                .commit();
    }
}