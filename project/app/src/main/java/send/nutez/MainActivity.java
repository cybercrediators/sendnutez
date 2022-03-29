package send.nutez;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import send.nutez.model.Nute;
import send.nutez.utils.LocalDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalDatabase.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        LocalDatabase.database.nuteDao().insertAll(new Nute("hello", "huhu"));
        Nute nute = LocalDatabase.database.nuteDao().findByName("hello");
        Log.d("NUTE_DEBUG", nute.getCategory());
    }
}