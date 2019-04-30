package online.lahloba.www.lahloba.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import online.lahloba.www.lahloba.data.database.dao.CartDao;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;

@Database(entities = {CartItemRoom.class}, version = 1)
public abstract class LahlobaDatabase extends RoomDatabase {
    private static volatile LahlobaDatabase INSTANCE;

    public static LahlobaDatabase newInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LahlobaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LahlobaDatabase.class, "lahloba_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract CartDao cartDao();

}
