package send.nutez;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.camera_layout, container, false);
        return (ViewGroup) view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        asdf = context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e("asdf", "VIEW CREATED");
        super.onViewCreated(view, savedInstanceState);
        previewView = view.findViewById(R.id.previewView);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), REQ_PERMISSIONS, REQ_CODE);
        }

        Button takePicture = view.findViewById(R.id.takePictureButton);
        takePicture.setOnClickListener(takePictureListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Pause", "PAUSER");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume", "RESUME");
        startCamera();
    }

    public void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(asdf);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                preview = new Preview.Builder().build();
                imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                //bindPreview(cameraProvider);
                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                Log.e("CAMERA ERROR", "Camera not initialized");
            }

        }, ContextCompat.getMainExecutor(asdf));
    }

    View.OnClickListener takePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (imageCapture != null) {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //File photoFile = new File(path, new SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".png");
                File photoFile = new File(path, "asdf.png");
                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

                imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(asdf),
                        new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                                Uri savedURI = Uri.fromFile(photoFile);
                                String msg = "Photo captured successfully: " + savedURI;
                                Toast.makeText(asdf, msg, Toast.LENGTH_SHORT).show();
                                Log.i("IMAGE TAKEn", "IMAGE WAS TAKEN" + msg);
                            }

                            @Override
                            public void onError(ImageCaptureException exception) {
                                Log.e("IMAGE ERROR", "Couldn't take image!");
                                Uri savedURI = Uri.fromFile(photoFile);
                                Log.e("PATH", savedURI.toString());
                                Log.e("ERROR: ", exception.toString());
                            }
                        });
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
