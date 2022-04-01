package send.nutez.Fragments;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import send.nutez.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        EditTextPreference threshold = findPreference("threshold");
        EditTextPreference nms_threshold = findPreference("nms_threshold");
        EditTextPreference months = findPreference("months");
        EditTextPreference weight = findPreference("weight");
        EditTextPreference water_intake = findPreference("water_amount");
        EditTextPreference years = findPreference("years");

        if (water_intake != null) {
            water_intake.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)){
                        return "Not set";
                    }
                    return "Current water intake (ml): " + text;
                }
            });
        }
        if (weight != null) {
            weight.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)){
                        return "Not set";
                    }
                    return "Current weight (kg): " + text;
                }
            });
        }
        if (years != null) {
            years.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)){
                        return "Not set";
                    }
                    return "Current age (years): " + text;
                }
            });
        }
        if (months != null) {
            months.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)){
                        return "Not set";
                    }
                    return "Current age (months): " + text;
                }
            });
        }

        if (nms_threshold != null) {
            nms_threshold.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            });
            nms_threshold.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)){
                        return "Not set";
                    }
                    return "Current nms_threshold: " + text;
                }
            });
        }
        if (threshold != null) {
            threshold.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            });
            threshold.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
                @Override
                public CharSequence provideSummary(EditTextPreference preference) {
                    String text = preference.getText();
                    if (TextUtils.isEmpty(text)){
                        return "Not set";
                    }
                    return "Current threshold: " + text;
                }
            });
        }

    }

}
