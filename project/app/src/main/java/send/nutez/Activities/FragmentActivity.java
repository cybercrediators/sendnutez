package send.nutez.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import send.nutez.R;

/**
 * Fragment activity manager
 */
public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }
}