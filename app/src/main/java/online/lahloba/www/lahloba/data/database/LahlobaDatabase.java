package online.lahloba.www.lahloba.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import online.lahloba.www.lahloba.data.database.converter.DoubleConverter;
import online.lahloba.www.lahloba.data.database.dao.CartDao;
import online.lahloba.www.lahloba.data.database.dao.MarketPlaceDao;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;

@Database(entities = {CartItemRoom.class, MarketPlace.class}, version = 1)
@TypeConverters({DoubleConverter.class})

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
    public abstract MarketPlaceDao marketPlaceDao();

}
