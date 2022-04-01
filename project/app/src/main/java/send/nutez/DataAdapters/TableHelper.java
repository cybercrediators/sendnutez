package send.nutez.DataAdapters;

import android.util.Log;

import java.util.Date;
import java.util.List;

import send.nutez.model.Ingredient;
import send.nutez.model.Meal;

public class TableHelper {

    public static String[][] mealsToTable(List<Meal> meals) {
        String[][] strMeals = new String[meals.size()][2];
        for (int i = 0; i < meals.size(); i++) {
            strMeals[i][1] = meals.get(i).getId().toString();
            strMeals[i][0] = meals.get(i).getName();
        }
        return strMeals;
    }

    public static String[][] singleMealToTable(Meal m) {
        List<Ingredient> is = m.getIngredients();
        String[][] strMeal = new String[is.size()][2];
        for (int i = 0; i < is.size(); i++) {
            strMeal[i][0] = is.get(i).getName();
            strMeal[i][1] = is.get(i).getInformations();
        }
        return strMeal;
    }

    public static String[][] datesToTable(List<Date> dates) {
        String[][] strDates = new String[dates.size()][1];
        for (int i = 0; i < dates.size(); i++) {
            strDates[i][0] = (dates.get(i).getYear() + 1900) + "-" + (dates.get(i).getMonth() + 1) + "-" + dates.get(i).getDate();
        }
        return strDates;
    }

}
