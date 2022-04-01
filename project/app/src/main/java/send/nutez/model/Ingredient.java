package send.nutez.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Ingredient {
    @Id(autoincrement = true)
    private Long id;

    @Property
    private String name;

    @Property
    private String informations;

    @Property
    private String unit;

    @ToMany(referencedJoinProperty = "ingredient_id")
    private List<IngredientNuteValue> nutrients; // TODO pro 100 ml/g ??

    @Property
    private float quantity;

    private long meal_id;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 942581853)
    private transient IngredientDao myDao;

    public void addIngredientNuteValue(IngredientNuteValue ingredientNuteValue) {
        ingredientNuteValue.setIngredient_id(id);
        nutrients.add(ingredientNuteValue);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformations() {
        return this.informations;
    }

    public void setInformations(String informations) {
        this.informations = informations;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getQuantity() {
        return this.quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public long getMeal_id() {
        return this.meal_id;
    }

    public void setMeal_id(long meal_id) {
        this.meal_id = meal_id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 118255854)
    public List<IngredientNuteValue> getNutrients() {
        if (nutrients == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IngredientNuteValueDao targetDao = daoSession
                    .getIngredientNuteValueDao();
            List<IngredientNuteValue> nutrientsNew = targetDao
                    ._queryIngredient_Nutrients(id);
            synchronized (this) {
                if (nutrients == null) {
                    nutrients = nutrientsNew;
                }
            }
        }
        return nutrients;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1930925108)
    public synchronized void resetNutrients() {
        nutrients = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1386056592)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIngredientDao() : null;
    }

    public Ingredient(String name, float quantity) {
        this.name = name;
        this.quantity = quantity;
        nutrients = new ArrayList<>();
    }

    @Generated(hash = 1862526597)
    public Ingredient(Long id, String name, String informations, String unit,
            float quantity, long meal_id) {
        this.id = id;
        this.name = name;
        this.informations = informations;
        this.unit = unit;
        this.quantity = quantity;
        this.meal_id = meal_id;
    }

    @Generated(hash = 1584798654)
    public Ingredient() {
    }
}
