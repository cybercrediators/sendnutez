package send.nutez.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import send.nutez.Fragments.SettingsFragment;
import send.nutez.MainActivity;
import send.nutez.R;
import send.nutez.model.Person;

/**
 * Activity, container for the settings fragment
 */
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


    /**
     * override the back button to update personal settings at the home screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.updatePerson(this);
    }
}