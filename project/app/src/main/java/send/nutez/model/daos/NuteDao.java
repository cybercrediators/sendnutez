package send.nutez.model.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import send.nutez.model.Nute;

@Dao
public interface NuteDao {
    @Query("SELECT * FROM nute")
    List<Nute> getAll();

    @Query("SELECT * FROM nute WHERE id IN (:nuteIds)")
    List<Nute> loadAllByIds(int[] nuteIds);

    @Query("SELECT * FROM nute WHERE name LIKE :name LIMIT 1")
    Nute findByName(String name);

    @Insert
    void insertAll(Nute... nutes);

    @Delete
    void delete(Nute nute);
}