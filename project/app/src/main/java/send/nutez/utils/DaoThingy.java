package send.nutez.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import send.nutez.model.Nute;
import send.nutez.model.daos.NuteDao;

@Database(entities = {Nute.class}, version = 1)
public abstract class DaoThingy extends RoomDatabase {
    public abstract NuteDao nuteDao();
}
