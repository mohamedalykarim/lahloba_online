package online.lahloba.www.lahloba.data.database.converter;

import androidx.room.TypeConverter;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import online.lahloba.www.lahloba.data.model.AddressItem;

public class AddressItemConverter    {
    @TypeConverter
    public String addressToString(AddressItem addressItem){
        Gson gson = new Gson();
        Type type = new TypeToken<AddressItem>() {}.getType();
        String json = gson.toJson(addressItem, type);
        return json;

    }

    @TypeConverter
    public AddressItem stringToAddress(String string){
        Gson gson = new Gson();
        Type type = new TypeToken<AddressItem>() {}.getType();
        AddressItem addressItem = gson.fromJson(string, type);
        return addressItem;
    }


}
