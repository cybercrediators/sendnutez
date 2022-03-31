package send.nutez.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Dictionary;
import java.util.Hashtable;

public class PermissionHandler {
    public static final String[] REQ_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    public static Dictionary<String, Boolean> permissions = new Hashtable<>();

    public static void initialize(Activity activity) {
        if (!allPermissionsGranted(activity.getApplicationContext())) {
            int REQ_CODE = 1;
            ActivityCompat.requestPermissions(activity, REQ_PERMISSIONS, REQ_CODE);
        }
    }

    private static boolean allPermissionsGranted(Context context) {
        boolean ret = true;
        for (String perm: REQ_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_DENIED) {
                permissions.put(perm, false);
                ret = false;
            } else {
                permissions.put(perm, true);
            }
        }
        return ret;
    }

}
