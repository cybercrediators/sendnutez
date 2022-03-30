package send.nutez.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import send.nutez.utils.StorageDatabaseUtils;

@Entity
public class Meal {
    @Id(autoincrement = true)
    private Long id;

    private Date creationDate;
    private String name;
    @ToMany(referencedJoinProperty = "meal_id")
    private List<Ingredient> ingredients;

    public void addIngredient(Ingredient ingredient) {
        ingredient.setMeal_id(id);
        ingredients.add(ingredient);
        StorageDatabaseUtils.insert(ingredient);
    }

    public Meal(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.creationDate = new Date(System.currentTimeMillis());
    }
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1947976862)
    private transient MealDao myDao;

    @Generated(hash = 979687567)
    public Meal(Long id, Date creationDate, String name) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
    }

    @Generated(hash = 167100247)
    public Meal() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1292705092)
    public List<Ingredient> getIngredients() {
        if (ingredients == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IngredientDao targetDao = daoSession.getIngredientDao();
            List<Ingredient> ingredientsNew = targetDao._queryMeal_Ingredients(id);
            synchronized (this) {
                if (ingredients == null) {
                    ingredients = ingredientsNew;
                }
            }
        }
        return ingredients;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 183837919)
    public synchronized void resetIngredients() {
        ingredients = null;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 644317336)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMealDao() : null;
    }
}
