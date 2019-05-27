package online.lahloba.www.lahloba.data.database.converter;

import android.arch.persistence.room.TypeConverter;

public class DoubleConverter {
    @TypeConverter
    public static Double toDouble(String value) {
        return value == null ? null : Double.parseDouble(value);
    }

    @TypeConverter
    public static String toString(Double value) {
        return value == null ? null : String.valueOf(value);
    }
}
