package send.nutez.utils.NutritionApiConnector;

import static send.nutez.MainActivity.DEBUG_STRING;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import send.nutez.model.Ingredient;
import send.nutez.model.IngredientNuteValue;
import send.nutez.model.Nute;
import send.nutez.model.Nutrient;
import send.nutez.utils.StorageDatabaseUtils;

public class AmilandNutritionApiConnector implements NutritionApiConnector {
    private static final String API_KEY = "2yzm7ATcl3JRuXlELbxHo681RHPO2448Ubj2KyM4";

    private static HttpUrl makeQuery(String mealName, int maxResults) {
        //String url = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=" + API_KEY + "&query=Cheddar%20Cheese&pageSize=10&pageNumber=1";
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.nal.usda.gov")
                .addPathSegments("fdc/v1/foods/search")
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("query", mealName)
                .addQueryParameter("pageSize", Integer.toString(maxResults))
                .addQueryParameter("pageNumber", "1")
                .build();
        return url;
    }

    @Override
    public void fillIngredientNutrients(Ingredient ingreed) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = makeQuery(ingreed.getName(), 1);
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            JSONObject o = new JSONObject(response.body().string());
            Map<String, String> nameMap = getNuteNameMap();

            if(o.has("foods")) {
                JSONArray foods = o.getJSONArray("foods");
                //Log.d(DEBUG_STRING, foods.toString(2));
                for(int i = 0; i < 1; i++) {
                    if(foods.getJSONObject(i).has("foodNutrients")) {
                        JSONArray nutrients = foods.getJSONObject(i).getJSONArray("foodNutrients");
                        for(int j = 0; j < nutrients.length(); j++) {
                            String nutrientName = nutrients.getJSONObject(j).getString("nutrientName");
                            if (nameMap.containsKey(nutrientName)) {
                                Nute n = StorageDatabaseUtils.getNuteByName(nameMap.get(nutrientName));
                                IngredientNuteValue ingredientNuteValue = new IngredientNuteValue();
                                ingredientNuteValue.setNute_id(n.id);
                                ingredientNuteValue.setValue(nutrients.getJSONObject(j).getDouble("value"));
                                ingreed.addIngredientNuteValue(ingredientNuteValue);
                                ingreed.setInformations("https://fdc.nal.usda.gov/fdc-app.html#/food-details/" + foods.getJSONObject(i).getInt("fdcId") + "/");
                                StorageDatabaseUtils.insert(ingredientNuteValue);
                            } else {
                                //TODO we don't yet have this Nute
                                //Log.d(DEBUG_STRING, nutrientName);
                            }
                        }
                    }
                }
            }
        } catch (IOException | JSONException e) {
            Log.d(DEBUG_STRING, url.toString());
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getNuteNameMap() {
        Map<String, String> ret = new HashMap<>();
        ret.put("", Nutrient.ALPHA_LINOLENIC_ACID.getName());
        ret.put("", Nutrient.EICOSAPENTAENOIC_ACID_DOCOSAHEXAENOIC_ACID_EPA_DHA.getName());
        ret.put("Biotin", Nutrient.BIOTIN.getName());
        ret.put("Water", Nutrient.WATER.getName());
        ret.put("Calcium, Ca", Nutrient.CALCIUM.getName());
        ret.put("Choline, total", Nutrient.CHOLINE.getName());
        ret.put("Vitamin B-12", Nutrient.COBALAMIN_VITAMIN_B12.getName());
        ret.put("Copper, Cu", Nutrient.COPPER.getName());
        ret.put("Fiber, total dietary", Nutrient.DIETARY_FIBRE.getName());
        ret.put("Energy", Nutrient.ENERGY.getName());
        ret.put("Fluoride, F", Nutrient.FLUORIDE.getName());
        ret.put("Folate, total", Nutrient.FOLATE.getName());
        ret.put("Iodine, I", Nutrient.IODINE.getName());
        ret.put("Iron, Fe", Nutrient.IRON.getName());
        ret.put("", Nutrient.LINOLEIC_ACID_LA.getName());
        ret.put("Magnesium, Mg", Nutrient.MAGNESIUM.getName());
        ret.put("Manganese, Mn", Nutrient.MANGANESE.getName());
        ret.put("Niacin", Nutrient.NIACIN.getName());
        ret.put("Chlorine, Cl", Nutrient.CHLORIDE.getName());
        ret.put("Molybdenum, Mo", Nutrient.MOLYBDENUM.getName());
        ret.put("", Nutrient.PANTOTHENIC_ACID.getName());
        ret.put("Phosphorus, P", Nutrient.PHOSPHORUS.getName());
        ret.put("Potassium, K", Nutrient.POTASSIUM.getName());
        ret.put("Protein", Nutrient.PROTEIN.getName());
        ret.put("Riboflavin", Nutrient.RIBOFLAVIN.getName());
        ret.put("Fatty acids, total saturated", Nutrient.SATURATED_FATTY_ACIDS_SFA.getName());
        ret.put("Selenium, Se", Nutrient.SELENIUM.getName());
        ret.put("Sodium, Na", Nutrient.SODIUM.getName());
        ret.put("Thiamin", Nutrient.THIAMIN.getName());
        ret.put("Carbohydrate, by difference", Nutrient.TOTAL_CARBOHYDRATES.getName());
        ret.put("", Nutrient.TOTAL_FAT.getName());
        ret.put("Fatty acids, total trans", Nutrient.TRANS_FATTY_ACIDS_TFA.getName());
        ret.put("Vitamin A, IU", Nutrient.VITAMIN_A.getName());
        ret.put("Vitamin B-6", Nutrient.VITAMIN_B6.getName());
        ret.put("Vitamin C, total ascorbic acid", Nutrient.VITAMIN_C.getName());
        ret.put("Vitamin D (D2 + D3)", Nutrient.VITAMIN_D.getName());
        ret.put("", Nutrient.VITAMIN_E_AS_ALPHA_TOCOPHEROL.getName());
        ret.put("", Nutrient.VITAMIN_K_AS_PHYLLOQUINONE.getName());
        ret.put("Zinc, Zn", Nutrient.ZINC.getName());
        return ret;
    }
}
