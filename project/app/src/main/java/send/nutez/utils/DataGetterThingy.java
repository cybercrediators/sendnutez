package send.nutez.utils;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import send.nutez.model.Ingredient;
import send.nutez.model.IngredientNuteValue;
import send.nutez.model.Nute;
import send.nutez.model.Meal;

public class DataGetterThingy {

    /**
     * TODO connect dr oethker
     * @param recipe
     * @return
     */
    public List<Ingredient> getRecipe(Meal recipe) {
        return new ArrayList<>();
    }

    public void fillNutrientsFor(Ingredient ingredient) {
        StorageDatabaseUtils.insert(ingredient);
        //TODO get nutrient list for ingredient name
        List<IngredientNuteValue> values = new ArrayList<>();

        for(IngredientNuteValue v : values) {
            v.setIngredient_id(ingredient.getId());
            v.setNute_id(0);
        }
    }

    public Dictionary<Nute, Float> getNutrientsFor(Meal meal) {
        return new Hashtable<>();
    }
}
