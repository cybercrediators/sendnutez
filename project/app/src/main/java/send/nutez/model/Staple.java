package send.nutez.model;

import java.util.Dictionary;
import java.util.Hashtable;

public class Staple extends Food {
    public Staple(String name, Dictionary<Nute, Float> nutrients) {
        this.name = name;
        this.nutrients = nutrients;
    }
    public Staple(String name) {
        this.name = name;
        this.nutrients = new Hashtable<>();
    }
}
