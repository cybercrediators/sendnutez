package send.nutez.Fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.camera.core.impl.ImageAnalysisConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import send.nutez.Activities.PredictionEdit;
import send.nutez.Overlays.RectOverlay;
import send.nutez.Prediction.Box;
import send.nutez.Prediction.Predictor;
import send.nutez.Prediction.YOLOv5;
import send.nutez.R;

public class CameraFragment extends Fragment {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Context asdf;
    private PreviewView previewView;
    private Camera camera;
    private String[] REQ_PERMISSIONS = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
    private int REQ_CODE = 12;
    private static String FILENAME_FORMAT = "dd-MM-yyyy-ss-SSS";
    private Preview preview;
    private ImageCapture imageCapture = null;
    private RectOverlay rectOverlay;
    private boolean USE_GPU;
    private String selectedPicture = "";
    private TextureView viewFinder;
    private Predictor predictor;
    ExecutorService detectService = Executors.newSingleThreadExecutor();
    private int width;
    private int height;
    private static double threshold = 0.15;
    private static double nms_threshold = 0.7;
    private AtomicBoolean livePreview = new AtomicBoolean(false);
    private AtomicBoolean isProcessing = new AtomicBoolean(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.camera_layout, container, false);
        livePreview.set(true);
        return (ViewGroup) view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        asdf = context;
        livePreview.set(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e("asdf", "VIEW CREATED");

        super.onViewCreated(view, savedInstanceState);
        previewView = view.findViewById(R.id.previewView);
        rectOverlay = view.findViewById(R.id.rectOver);
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), REQ_PERMISSIONS, REQ_CODE);
        }

        Button takePicture = view.findViewById(R.id.takePictureButton);
        Button selectPicture = view.findViewById(R.id.selectButton);
        takePicture.setOnClickListener(takePictureListener);
        selectPicture.setOnClickListener(switchViewListener);

        initModel();
        initViewListener();
    }

    protected void initViewListener() {
        float threshold = 0.3f;
        float nms_threshold = 0.7f;
        predictor = new Predictor();
    }

    protected void initModel() {
        YOLOv5.init(getActivity().getAssets(), true);
    }

    private void updateTransform() {
        Matrix matrix = new Matrix();
        // Compute the center of the view finder
        float centerX = viewFinder.getWidth() / 2f;
        float centerY = viewFinder.getHeight() / 2f;

        float[] rotations = {0, 90, 180, 270};
        // Correct preview output to account for display rotation
        float rotationDegrees = rotations[viewFinder.getDisplay().getRotation()];

        matrix.postRotate(-rotationDegrees, centerX, centerY);

        // Finally, apply transformations to our TextureView
        viewFinder.setTransform(matrix);
    }

    @Override
    public void onPause() {
        super.onPause();
        livePreview.set(false);
        Log.e("Pause", "PAUSER");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume", "RESUME");
        livePreview.set(true);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        threshold = Double.parseDouble(sharedPreferences.getString("threshold", "0.15"));
        nms_threshold = Double.parseDouble(sharedPreferences.getString("nms_threshold", "0.7"));

        startCamera();
    }

    public void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(asdf);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                preview = new Preview.Builder()
                        .setTargetResolution(new Size(previewView.getWidth(), previewView.getHeight()))
                        .build();
                imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                //bindPreview(cameraProvider);
                cameraProvider.unbindAll();
                DetectAnalyzer detectAnalyzer = new DetectAnalyzer();
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(previewView.getWidth(), previewView.getHeight()))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), detectAnalyzer);
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview, imageAnalysis);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                Log.e("CAMERA ERROR", "Camera not initialized");
            }

        }, ContextCompat.getMainExecutor(asdf));
    }

    private class DetectAnalyzer implements ImageAnalysis.Analyzer {

        @Override
        public void analyze(@NonNull ImageProxy image) {
            if (!livePreview.get())
                return;
            if (isProcessing.get()) {
                image.close();
                return;
            }
            int rotationDegrees = image.getImageInfo().getRotationDegrees();
            liveDetection(image, rotationDegrees);
            image.close();
        }
    }

    private void liveDetection(ImageProxy img, final int rotationDegrees) {
        isProcessing.set(true);
        final Bitmap src = proxyToBitmap(img);
        img.close();
        if (detectService == null) {
            //Toast.makeText(this, "No detection possible!", Toast.LENGTH_SHORT).show();
            Log.e("NO DET.", "NO detection possible!");
            return;
        }
        detectService.execute(new Runnable() {
            @Override
            public void run() {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotationDegrees);
                width = src.getWidth();
                height = src.getHeight();
                Bitmap bmp = Bitmap.createBitmap(src, 0, 0, width, height, matrix, false);
                //predictor.detectFromImage(bmp, threshold, nms_threshold, true);
                Box[] result = predictor.getResults(bmp, threshold, nms_threshold);
                if (result != null) {
                    showResultOnUI(result, bmp);
                }
            }
        });
    }

    protected void showResultOnUI(Box[] result, Bitmap bmp) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rectOverlay.drawBoxRects(result, bmp.getWidth());
                isProcessing.set(false);
            }
        });
    }

    private Bitmap proxyToBitmap(ImageProxy img) {
        ImageProxy.PlaneProxy[] planes = img.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, img.getWidth(), img.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private void takePhoto() throws IOException {

        if (imageCapture != null) {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            //File photoFile = new File(path, );
            String fname = new SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis());
            File folder = new File(path, "asdf");
            if( !folder.exists() )
                folder.mkdir();

            long timestamp = System.currentTimeMillis();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

            //File photoFile = File.createTempFile(fname, ".jpg", folder);
            File photoFile = new File(new File(path), timestamp + ".jpg");
            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                    this.asdf.getContentResolver(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
            ).build();

            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(asdf),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                            Uri savedURI = Uri.fromFile(photoFile);
                            String msg = "Photo captured successfully: " + savedURI;
                            Toast.makeText(asdf, msg, Toast.LENGTH_SHORT).show();
                            Log.i("IMAGE TAKEn", "IMAGE WAS TAKEN" + msg);
                            selectedPicture = savedURI.toString();
                            PredictionEdit.selectImageToggle = false;
                            switchToView();
                        }

                        @Override
                        public void onError(ImageCaptureException exception) {
                            Log.e("IMAGE ERROR", "Couldn't take image!");
                            Uri savedURI = Uri.fromFile(photoFile);
                            Log.e("PATH", savedURI.toString());
                            Log.e("ERROR: ", exception.toString());
                            exception.printStackTrace();
                        }
                    });
        }
    }

    View.OnClickListener switchViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectedPicture = "";
            PredictionEdit.selectImageToggle = true;
            switchToView();
        }
    };

    private void switchToView() {
        // start other Fragment activity
        PredictionEdit.filepath = selectedPicture;
        Intent intent = new Intent(getActivity(), PredictionEdit.class);
        getActivity().startActivity(intent);
    }

    View.OnClickListener takePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                takePhoto();
            } catch (IOException e) {
                Log.e("CRINGE", "YOU FAILED");
                e.printStackTrace();
            }
        }
    };

    private boolean allPermissionsGranted() {
        for (String perm: REQ_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(asdf, perm) == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }
}
