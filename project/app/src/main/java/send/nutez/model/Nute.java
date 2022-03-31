package send.nutez.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity(indexes = {@Index(value = "name", unique = true)}) //makes the name col unique
public class Nute {

    @Id(autoincrement = true)
    public Long id;

    @Property(nameInDb = "category")
    private String category;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "unit")
    private String unit;

    public Nute(String name, String category) {
        this.name = name;
        this.category = category;
    }

    @Generated(hash = 776708133)
    public Nute(Long id, String category, String name, String unit) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.unit = unit;
    }

    @Generated(hash = 665657666)
    public Nute() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
