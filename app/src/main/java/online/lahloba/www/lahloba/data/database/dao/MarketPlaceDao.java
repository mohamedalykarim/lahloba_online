package online.lahloba.www.lahloba.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import online.lahloba.www.lahloba.data.model.MarketPlace;

@Dao
public interface MarketPlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MarketPlace item);

    @Query("DELETE FROM market_places")
    void deleteAll();

    @Query("SELECT * from market_places")
    LiveData<List<MarketPlace>> getAll();

    @Query("SELECT COUNT(id) FROM market_places")
    int getCount();

    @Query("SELECT * from market_places WHERE id IN(:ids)")
    LiveData<List<MarketPlace>> getSpecificMarketPlaces(List<String> ids);

    @Query("SELECT * from market_places WHERE id = :id")
    LiveData<MarketPlace> getSpecificMarketPlace(String id);
}
