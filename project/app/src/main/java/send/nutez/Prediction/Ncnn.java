package send.nutez.Prediction;

import android.app.Application;
import android.content.Context;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;
import android.util.Log;

import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;

import send.nutez.AppCrashHandler;
import send.nutez.MainActivity;

public class Ncnn extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initRecovery();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private void initRecovery() {
        Recovery.getInstance()
                .debug(BuildConfig.DEBUG)
                .recoverInBackground(true)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .recoverEnabled(true)
                .callback(new MyCrashCallback())
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
//                .skip(TestActivity.class)
                .init(this);
        AppCrashHandler.register();
    }

    static final class MyCrashCallback implements RecoveryCallback {
        @Override
        public void stackTrace(String exceptionMessage) {
            Log.e("Error", "exceptionMessage:" + exceptionMessage);
        }

        @Override
        public void cause(String cause) {
            Log.e("Eroor", "cause:" + cause);
        }

        @Override
        public void exception(String exceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
            Log.e("Error", "exceptionClassName:" + exceptionType);
            Log.e("Error", "throwClassName:" + throwClassName);
            Log.e("Error", "throwMethodName:" + throwMethodName);
            Log.e("Error", "throwLineNumber:" + throwLineNumber);
        }

        @Override
        public void throwable(Throwable throwable) {

        }
    }

}
