package send.nutez.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import send.nutez.Prediction.Predictor;
import send.nutez.R;

public class PredictionEdit extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    public static String filepath;
    private TextView predictionText;
    private Button selectImageButton;
    private Button discardButton;
    private Button continueButton;
    private Bitmap mutableBitmap;
    private ImageView preview;
    public static boolean selectImageToggle = false;
    private static double threshold = 0.15;
    private static double nms_threshold = 0.7;

    private Predictor predictor;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prediction_edit_layout);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        threshold = Double.parseDouble(sharedPreferences.getString("threshold", ""));
        nms_threshold = Double.parseDouble(sharedPreferences.getString("nms_threshold", ""));

        Log.e("asdf", " "+filepath);
        // TODO: CHECK IF YOU SELECTED IMAGE IN CAMERA FRAGMENT
        initID();
        predictor = new Predictor();
        initListeners();
    }

    private void initID() {
        predictionText = findViewById(R.id.predictionText);
        selectImageButton = findViewById(R.id.selectImageButton);
        preview = findViewById(R.id.editPreviewView);
        discardButton = findViewById(R.id.discardButton);
        continueButton = findViewById(R.id.continueButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null)
                Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
            // pls don't ask
            checkPicture(Uri.parse(getPathFromURI(data.getData())));
        } else {
            Toast.makeText(this, "Failed to pick an image!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        filepath = res;
        return res;
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    View.OnClickListener continueSelection = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            HashMap<String, List<Float>> predictions = predictor.getLastPredictedClasses();
            Intent cont = new Intent(PredictionEdit.this, IngredientEditorActivity.class);
            cont.putExtra("PRED_LIST", predictions);
            cont.putExtra("IMG_PATH", filepath);
            startActivity(cont);
        }
    };

    View.OnClickListener discardSelection = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    View.OnClickListener selectPictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectImageFromGallery();
        }
    };

    private void initListeners() {
        // Choose image if you picked select image in the camera fragment
        // filepath = "file:///storage/emulated/0/Pictures/1648651002271.jpg";
        selectImageButton.setOnClickListener(selectPictureListener);
        discardButton.setOnClickListener(discardSelection);
        continueButton.setOnClickListener(continueSelection);

        if (selectImageToggle && filepath.isEmpty()) {
            selectImageToggle = false;
            selectImageFromGallery();
        } else if (!filepath.isEmpty()) {
            Uri path = Uri.parse(filepath);
            checkPicture(path);
        } else {
            Toast.makeText(this, "NO picture was selected or taken!", Toast.LENGTH_LONG).show();
        }
    }

    public void checkPicture(Uri filepath) {
        String[] REQ_PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
        final Bitmap image = predictor.getPicture(filepath, getContentResolver());
        preview.setImageURI(filepath);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                mutableBitmap = image.copy(Bitmap.Config.ARGB_8888, true);
                width = image.getWidth();
                height = image.getHeight();

                mutableBitmap = predictor.detectFromImage(mutableBitmap, threshold, nms_threshold);
                final long dur = System.currentTimeMillis() - start;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        preview.setImageBitmap(mutableBitmap);
                        if (predictor.getLastPredictedClasses() != null) {
                            Log.e("RESULT2", "" +predictor.getLastPredictedClasses().size());
                            predictionText.setText(predictor.getLastPredictedClasses().toString());
                        }
                    }
                });
            }
        }, "detection");
        thread.start();
    }
}