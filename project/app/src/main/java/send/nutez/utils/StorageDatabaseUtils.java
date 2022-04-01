package send.nutez.utils;

import static send.nutez.MainActivity.DEBUG_STRING;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import send.nutez.model.DaoMaster;
import send.nutez.model.DaoSession;
import send.nutez.model.Ingredient;
import send.nutez.model.IngredientNuteValue;
import send.nutez.model.Meal;
import send.nutez.model.MealDao;
import send.nutez.model.Nute;
import send.nutez.model.NuteDao;
import send.nutez.model.NuteReferenceValue;
import send.nutez.model.NuteReferenceValueDao;
import send.nutez.model.Nutrient;
import send.nutez.model.Person;
import send.nutez.model.PersonNuteReferences;

public class StorageDatabaseUtils {
    private static Database database;
    private static DaoSession daoSession;
    private static final String name = "storage.db";

    public static void initialize(Context context, DaoSession copiedNuteDaoSession) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, name);
        database = helper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();

        // copy entries from one database to other
        // highly inefficient and not well googled solution
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

    public static void update(Object obj) {
        daoSession.update(obj);
    }

    public static List<Nute> getAllNutes() {
        return daoSession.getNuteDao().loadAll();
    }

    public static Meal getMealByID(long id) {
        try {
            return daoSession.getMealDao().queryBuilder().where(MealDao.Properties.Id.eq(id)).limit(1).unique();
        } catch (Exception e) {
            Log.e(DEBUG_STRING, e.getMessage());
        }
        return null;

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

    public static List<Meal> getMealsForDay(long day) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(day);
        return getMealsForDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
    }

    public static List<Meal> getMealsForDay(int year, int month, int date) {
        GregorianCalendar start = new GregorianCalendar(year, month, date);
        GregorianCalendar end = new GregorianCalendar(year, month, date, 23, 59, 59);
        return getMealsBetween(new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()));
    }

    public static List<Meal> getMealsBetween(Date start, Date end) {
        try {
            return daoSession.getMealDao().queryBuilder().where(MealDao.Properties.CreationDate.between(start.getTime(), end.getTime())).list();
        } catch (Exception e) {
            Log.e(DEBUG_STRING, e.getMessage());
        }
        return null;
    }

    public static PersonNuteReferences getPersonNuteReferences(Person person) {
        PersonNuteReferences personNuteReferences = new PersonNuteReferences();
        for(Nute n : getAllNutes()) {
            try {
                List<NuteReferenceValue> val =  daoSession.getNuteReferenceValueDao().queryBuilder()
                        .where(NuteReferenceValueDao.Properties.NuteId.eq(n.id))
                        .where(NuteReferenceValueDao.Properties.AgeFrom.le(person.getAge()))
                        .where(NuteReferenceValueDao.Properties.AgeTo.ge(person.getAge()))
                        //.where(NuteReferenceValueDao.Properties.Gender.eq(person.getGender()))
                        .list();
                List<NuteReferenceValue> tmp = new ArrayList<>();
                for(NuteReferenceValue v : val) {
                    if(v.getGender().equals(person.getGender()))
                        tmp.add(v);
                }
                if(tmp.size() == 0)
                    tmp = val;

                List<NuteReferenceValue> ret = new ArrayList<>();
                float fitness = 0.0f;
                for(NuteReferenceValue v : tmp) {
                    if(v.getFitnessValue() > 0.0) {
                        if(v.getFitnessValue() > fitness && v.getFitnessValue() <= person.getFitness_level().getLevel()) {
                            ret.add(0, v);
                            fitness = v.getFitnessValue();
                        }
                    }
                }

                if(ret.size() == 0) {
                    if(val.size() != 0) {
                        personNuteReferences.referenceValueMap.put(n.getName(), val.get(0));
                    }
                } else {
                    personNuteReferences.referenceValueMap.put(n.getName(), ret.get(0));
                }
            } catch (Exception e) {
                Log.e(DEBUG_STRING, e.getMessage());
            }
        }
        return personNuteReferences;
    }
}
