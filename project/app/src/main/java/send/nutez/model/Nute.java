package send.nutez.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name"}, unique = true)}) //makes the name col unique
public class Nute {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "name")
    private String name;

    public Nute(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
