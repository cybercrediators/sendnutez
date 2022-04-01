package send.nutez.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import send.nutez.model.Meal;
import send.nutez.model.Nute;
import send.nutez.model.NuteReferenceValue;
import send.nutez.model.Person;
import send.nutez.model.PersonNuteReferences;
import send.nutez.model.Unit;

public class ScoreCalculator {
    private static Person getPerson() {
        return new Person();
    }

    public static int getFoodPercentage(long dayMillis) {
        Person person = getPerson();
        PersonNuteReferences personNuteReferences = StorageDatabaseUtils.getPersonNuteReferences(person);
        List<Meal> mealList = StorageDatabaseUtils.getMealsForDay(dayMillis);
        Map<String, Double> nuteValues = new HashMap<>();

        for(Nute n: StorageDatabaseUtils.getAllNutes()) {
            double val = 0.0;
            for(Meal m : mealList) {
                val += m.getTotalNutrientValue(n);
            }
            nuteValues.put(n.getName(), val);
        }
        double ret = 0.0;
        int c = 0;
        for(String nuteName : nuteValues.keySet()) {
            double val = nuteValues.get(nuteName);
            NuteReferenceValue nuteReferenceValue = personNuteReferences.referenceValueMap.get(nuteName);
            double ref = nuteReferenceValue.getReference_value();
            double percentage = val / nuteReferenceValue.getReference_value();

            if(ref == 0.0)
                percentage = 0;
            if(percentage > 1) {
                double tmp = percentage - (int) percentage;
                if(tmp > 1) { //TODO if too much tell user
                    percentage = 0;
                } else {
                    percentage -= tmp;
                }
            }
            ret += percentage;
            c++;
        }
        if(c > 0)
            return Math.round((float)(ret/(float) c));
        return 0;
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
