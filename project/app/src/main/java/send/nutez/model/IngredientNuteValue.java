package send.nutez.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class IngredientNuteValue {
    @Id(autoincrement = true)
    private Long id;

    private long nute_id;

    @ToOne(joinProperty = "nute_id")
    private Nute nute;

    private long ingredient_id;
    private double value;

    public IngredientNuteValue(Nute nute, float value) {
        this.nute = nute;
        this.nute_id = nute.id;
        this.value = value;
    }
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1558698494)
    private transient IngredientNuteValueDao myDao;
    @Generated(hash = 308095262)
    public IngredientNuteValue(Long id, long nute_id, long ingredient_id, double value) {
        this.id = id;
        this.nute_id = nute_id;
        this.ingredient_id = ingredient_id;
        this.value = value;
    }
    @Generated(hash = 1243942191)
    public IngredientNuteValue() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getNute_id() {
        return this.nute_id;
    }
    public void setNute_id(long nute_id) {
        this.nute_id = nute_id;
    }
    public long getIngredient_id() {
        return this.ingredient_id;
    }
    public void setIngredient_id(long ingredient_id) {
        this.ingredient_id = ingredient_id;
    }
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    @Generated(hash = 1980786776)
    private transient Long nute__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 555946562)
    public Nute getNute() {
        long __key = this.nute_id;
        if (nute__resolvedKey == null || !nute__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NuteDao targetDao = daoSession.getNuteDao();
            Nute nuteNew = targetDao.load(__key);
            synchronized (this) {
                nute = nuteNew;
                nute__resolvedKey = __key;
            }
        }
        return nute;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 568461808)
    public void setNute(@NotNull Nute nute) {
        if (nute == null) {
            throw new DaoException(
                    "To-one property 'nute_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.nute = nute;
            nute_id = nute.getId();
            nute__resolvedKey = nute_id;
        }
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
    @Generated(hash = 1473903398)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIngredientNuteValueDao() : null;
    }
}
