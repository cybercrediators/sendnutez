package send.nutez.utils;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import send.nutez.MainActivity;
import send.nutez.model.Meal;
import send.nutez.model.Nute;
import send.nutez.model.NuteReferenceValue;
import send.nutez.model.Person;
import send.nutez.model.PersonNuteReferences;
import send.nutez.model.Unit;

public class ScoreCalculator {
    private static Person getPerson() {
        return MainActivity.person;
    }

    public static String[][] getDayDetailTable(long dayMillis) {
        Map<String, Double> map = getDayDetails(dayMillis);
        String[][] ret = new String[map.keySet().size()][2];

        int i = 0;
        for(String s : map.keySet()) {
            ret[i][0] = s;
            ret[i][1] = Long.toString(Math.round(getNutrientPercentage(s, map.get(s))));
            i++;
        }
        return ret;
    }

    public static String[][] getMealDetailTable(Meal meal) {
        Map<String, Double> map = getMealDetails(meal);
        String[][] ret = new String[map.keySet().size()][2];

        int i = 0;
        for(String s : map.keySet()) {
            ret[i][0] = s;
            ret[i][1] = Long.toString(Math.round(getNutrientPercentage(s, map.get(s))));
            i++;
        }
        return ret;
    }

    private static double getNutrientPercentage(String nuteName, double value) {
        Person person = getPerson();
        PersonNuteReferences personNuteReferences = StorageDatabaseUtils.getPersonNuteReferences(person);
        NuteReferenceValue nuteReferenceValue = personNuteReferences.referenceValueMap.get(nuteName);
        double ref = nuteReferenceValue.getReference_value();
        if(ref == 0.0)
            ref = 1.0;

        double percentage = value / ref;
        return percentage;
    }

    public static int getFoodPercentage(long dayMillis) {
        Map<String, Double> nuteValues = getDayDetails(dayMillis);

        double ret = 0.0;
        int c = 0;

        for(String nuteName : nuteValues.keySet()) {
            double val = nuteValues.get(nuteName);
            double percentage = getNutrientPercentage(nuteName, val);
            ret += percentage;
            c++;
        }

        if(c > 0) {
            return Math.round((float) (ret / (float) c));
        }
        return 0;
    }

    public static Map<String, Double> getMealDetails(Meal meal) {
        Map<String, Double> nuteValues = new HashMap<>();

        for(Nute n: StorageDatabaseUtils.getAllNutes()) {
            double val = 0.0;
            val += meal.getTotalNutrientValue(n);
            nuteValues.put(n.getName(), val);
        }
        return nuteValues;
    }

    public static Map<String, Double> getDayDetails(long dayMillis) {
        List<Meal> mealList = StorageDatabaseUtils.getMealsForDay(dayMillis);
        Map<String, Double> nuteValues = new HashMap<>();

        for(Meal m : mealList) {
            Map<String, Double> mealDetails = getMealDetails(m);
            for(String nuteName : mealDetails.keySet()) {
                double val = 0.0;
                if(nuteValues.containsKey(nuteName)) {
                    val = nuteValues.get(nuteName);
                }
                val += mealDetails.get(nuteName);
                nuteValues.put(nuteName, val);
            }
        }
        return nuteValues;
    }

    public static int getWaterPercentage(long dayMillis) {
        Person person = getPerson();
        List<Meal> mealList = StorageDatabaseUtils.getMealsForDay(dayMillis);
        float ret = 0.0f;
        PersonNuteReferences personNuteReferences = StorageDatabaseUtils.getPersonNuteReferences(person);
        NuteReferenceValue nuteReferenceValue = personNuteReferences.referenceValueMap.get("Water");
        for(Meal m : mealList) {
            if(m.getName().equals("WATER") && m.getIngredients().size() == 1) {
                ret += Unit.ml.convert(Unit.L, m.getIngredients().get(0).getQuantity());
            }
        }
        float val = ret / nuteReferenceValue.getReference_value();
        return Math.round(val * 100);
    }
}
