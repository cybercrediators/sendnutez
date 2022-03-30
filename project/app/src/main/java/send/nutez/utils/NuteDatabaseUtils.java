package send.nutez.utils;

import static send.nutez.MainActivity.DEBUG_STRING;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import send.nutez.model.DaoMaster;
import send.nutez.model.DaoSession;
import send.nutez.model.Nute;
import send.nutez.model.NuteDao;
import send.nutez.model.NuteReferenceValue;
import send.nutez.model.NuteReferenceValueDao;

public class NuteDatabaseUtils {
    private static NuteDao nuteDao;
    private static NuteReferenceValueDao nuteReferenceValueDao;

    public static void initialize(Context context) {
        new NuteDatabaseOpenHelper(context).copyDataBase();
        Database nuteDatabase = new NuteDatabaseOpenHelper(context).getReadableDb();
        DaoSession daoSession = new DaoMaster(nuteDatabase).newSession();
        StorageDatabaseUtils.initialize(context, daoSession);
        nuteDao = daoSession.getNuteDao();
        nuteReferenceValueDao = daoSession.getNuteReferenceValueDao();
    }
}
