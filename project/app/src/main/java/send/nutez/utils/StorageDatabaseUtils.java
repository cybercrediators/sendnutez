package send.nutez.utils;

import static send.nutez.MainActivity.DEBUG_STRING;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import send.nutez.model.DaoMaster;
import send.nutez.model.DaoSession;
import send.nutez.model.Meal;
import send.nutez.model.Nute;
import send.nutez.model.NuteDao;
import send.nutez.model.NuteReferenceValue;
import send.nutez.model.NuteReferenceValueDao;

public class StorageDatabaseUtils {
    private static Database database;
    private static DaoSession daoSession;
    private static final String name = "storage.db";

    public static void initialize(Context context, DaoSession copiedNuteDaoSession) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, name);
        database = helper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();

        // copy entries from one database to other
        if(daoSession.getNuteDao().loadAll().size() == 0) {
            for (Nute n : copiedNuteDaoSession.getNuteDao().loadAll()) {
                daoSession.getNuteDao().insert(n);
            }
        }

        if(daoSession.getNuteReferenceValueDao().loadAll().size() == 0) {
            for (NuteReferenceValue n : copiedNuteDaoSession.getNuteReferenceValueDao().loadAll()) {
                daoSession.getNuteReferenceValueDao().insert(n);
            }
        }
    }

    public static List<Meal> getAllMeals() {
        return daoSession.getMealDao().loadAll();
    }

    public static void insert(Object obj) {
        daoSession.insertOrReplace(obj);
    }



    public static List<Nute> getAllNutes() {
        return daoSession.getNuteDao().loadAll();
    }

    public static Nute getNuteByName(String name) {
        try {
            return daoSession.getNuteDao().queryBuilder().where(NuteDao.Properties.Name.eq(name)).limit(1).unique();
        } catch (Exception e) {
            Log.e(DEBUG_STRING, e.getMessage());
        }
        return null;
    }

    public static List<NuteReferenceValue> getNuteReferenceValueByName(String name) {
        try {
            Nute nute = getNuteByName(name);
            if(nute != null)
                return daoSession.getNuteReferenceValueDao().queryBuilder().where(NuteReferenceValueDao.Properties.NuteId.eq(nute.id)).list();
        } catch (Exception e) {
            Log.e(DEBUG_STRING, e.getMessage());
        }
        return null;
    }
}
