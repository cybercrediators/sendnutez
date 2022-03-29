package send.nutez.utils;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import send.nutez.model.Food;
import send.nutez.model.Nute;
import send.nutez.model.Recipe;
import send.nutez.model.Staple;

public class DataGetterThingy {

    /**
     * TODO connect dr oethker
     * @param recipe
     * @return
     */
    public List<Staple> getRecipe(Recipe recipe) {
        return new ArrayList<>();
    }

    public Dictionary<Nute, Float> getNutrientsFor(Staple staple) {
        return new Hashtable<>();
    }

    public Dictionary<Nute, Float> getNutrientsFor(Food staple) {
        return new Hashtable<>();
    }

    public Dictionary<Nute, Float> getNutrientsFor(Recipe staple) {
        return new Hashtable<>();
    }
}
