package send.nutez.utils.NutritionApiConnector;

import java.util.Map;

import send.nutez.model.Ingredient;

public interface NutritionApiConnector {
    /**
     * Fills given ingredient with nutrient data
     * @param ingreed
     */
    void fillIngredientNutrients(Ingredient ingreed);
    public Map<String, String> getNuteNameMap();
}
