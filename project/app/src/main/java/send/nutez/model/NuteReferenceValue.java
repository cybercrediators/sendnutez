package send.nutez.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import java.lang.String;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class NuteReferenceValue {
    public enum Gender {
        Female,
        Male
    }

    @Id(autoincrement = true)
    public Long id;

    private long nuteId;

    @ToOne(joinProperty = "nuteId")
    private Nute nute;

    @Property(nameInDb = "target_population")
    private java.lang.String targetPopulation;

    @Property(nameInDb = "age_from")
    private int ageFrom; //months

    @Property(nameInDb = "age_to")
    private int ageTo; //months

    @Property(nameInDb = "gender")
    private String gender;

    @Property(nameInDb = "fitness_value")
    private float fitnessValue; //pal

    @Property(nameInDb = "reference_value")
    private float reference_value;

    @Property(nameInDb = "upper_limit")
    private float upper_limit;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 800679211)
    private transient NuteReferenceValueDao myDao;

    @Generated(hash = 1980786776)
    private transient Long nute__resolvedKey;

    @Generated(hash = 194234474)
    public NuteReferenceValue(Long id, long nuteId, java.lang.String targetPopulation,
            int ageFrom, int ageTo, String gender, float fitnessValue, float reference_value,
            float upper_limit) {
        this.id = id;
        this.nuteId = nuteId;
        this.targetPopulation = targetPopulation;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.gender = gender;
        this.fitnessValue = fitnessValue;
        this.reference_value = reference_value;
        this.upper_limit = upper_limit;
    }

    @Generated(hash = 1249619833)
    public NuteReferenceValue() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.lang.String getTargetPopulation() {
        return this.targetPopulation;
    }

    public void setTargetPopulation(java.lang.String targetPopulation) {
        this.targetPopulation = targetPopulation;
    }

    public int getAgeFrom() {
        return this.ageFrom;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public int getAgeTo() {
        return this.ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getFitnessValue() {
        return this.fitnessValue;
    }

    public void setFitnessValue(float fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public float getReference_value() {
        return this.reference_value;
    }

    public void setReference_value(float reference_value) {
        this.reference_value = reference_value;
    }

    public float getUpper_limit() {
        return this.upper_limit;
    }

    public void setUpper_limit(float upper_limit) {
        this.upper_limit = upper_limit;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1385985292)
    public Nute getNute() {
        long __key = this.nuteId;
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
    @Generated(hash = 1236648195)
    public void setNute(@NotNull Nute nute) {
        if (nute == null) {
            throw new DaoException(
                    "To-one property 'nuteId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.nute = nute;
            nuteId = nute.getId();
            nute__resolvedKey = nuteId;
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

    public long getNuteId() {
        return this.nuteId;
    }

    public void setNuteId(long nuteId) {
        this.nuteId = nuteId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 79036390)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNuteReferenceValueDao() : null;
    }
}