package send.nutez.utils;

import android.content.Context;

import androidx.room.Room;

public class LocalDatabase {
    public static DaoThingy database;
    public static void initialize(Context context) {
        database = Room.databaseBuilder(context, DaoThingy.class, "local-database")
                .allowMainThreadQueries().build();
    }
}
