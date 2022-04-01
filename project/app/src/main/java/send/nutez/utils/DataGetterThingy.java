package send.nutez.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kotlin.coroutines.Continuation;
import send.nutez.model.Ingredient;
import send.nutez.model.IngredientNuteValue;
import send.nutez.model.Meal;
import send.nutez.model.Nutrient;
import send.nutez.utils.NutritionApiConnector.AmilandNutritionApiConnector;
import send.nutez.utils.NutritionApiConnector.NutritionApiConnector;

public class DataGetterThingy {
    public List<Ingredient> getRecipe(Meal recipe) {
        return new ArrayList<>();
    }
    private static NutritionApiConnector nutritionApiConnector = new AmilandNutritionApiConnector();

    public static void getMeal(Map<String, Float> ingredients, Continuation<Meal> continuation) {
        getMeal(ingredients, continuation, null);
    }

    public static void getMeal(Map<String, Float> ingredients, Continuation<Meal> continuation, String name) {
        boolean nullName = name == null;
        if(nullName)
            name = "meal";
        String finalName = name;
        new ApplicationExecutors().getBackground().execute( () -> {
            Meal meal = new Meal(finalName);
            StorageDatabaseUtils.insert(meal);
            if(nullName)
                meal.setName(meal.getName() + " " + meal.getId());

            StorageDatabaseUtils.update(meal);

            for(String in : ingredients.keySet()) {
                Ingredient ingredient = new Ingredient(in, ingredients.get(in));
                meal.addIngredient(ingredient);
                nutritionApiConnector.fillIngredientNutrients(ingredient);
                StorageDatabaseUtils.insert(ingredient);
            }
            StorageDatabaseUtils.update(meal);
            new ApplicationExecutors().getMainThread().execute(
                () -> {
                    continuation.resumeWith(meal);
                }
            );
        });
    }


    public static void addWater(float amount, Continuation<Boolean> continuation) {
        new ApplicationExecutors().getBackground().execute( () -> {
            Meal water = new Meal("WATER");
            StorageDatabaseUtils.insert(water);

            Ingredient ingredient = new Ingredient("Water", amount);
            ingredient.setUnit("ml");
            ingredient.setInformations("added by water button");

            water.addIngredient(ingredient);

            IngredientNuteValue ingredientNuteValue = new IngredientNuteValue();
            ingredientNuteValue.setNute(StorageDatabaseUtils.getNuteByName(Nutrient.WATER.getName()));
            ingredientNuteValue.setUnit("ml");
            ingredientNuteValue.setNute_id(ingredientNuteValue.getNute().id);
            ingredientNuteValue.setValue(amount);

            StorageDatabaseUtils.insert(ingredient);
            ingredient.addIngredientNuteValue(ingredientNuteValue);
            StorageDatabaseUtils.insert(ingredientNuteValue);

            new ApplicationExecutors().getMainThread().execute(
                    () -> {
                        continuation.resumeWith(true);
                    }
            );
        });
    }
}
