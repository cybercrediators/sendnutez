package send.nutez.model;

public class Ingredient {
    public enum Unit {
        ml,
        g,
    }

    private Staple staple;
    private float quantity;
    private Unit unit;
}
