package online.lahloba.www.lahloba.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;

@Dao
public interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CartItemRoom item);

    @Query("DELETE FROM cart")
    void deleteAll();

    @Query("DELETE FROM cart WHERE count < 1")
    void deleteAllCount0();

    @Query("SELECT * from cart")
    LiveData<List<CartItemRoom>> getAll();

    @Query("SELECT * from cart WHERE count > 0")
    LiveData<List<CartItemRoom>> getAllWithCount();

    @Query("UPDATE cart SET count = :count WHERE productId = :id AND count > 0")
    void changeCount(String id, int count);

    @Query("SELECT * from cart WHERE productId = :productId")
    LiveData<CartItemRoom> getSpecificCartItem(String productId);

    @Query("DELETE FROM cart WHERE productId = :id")
    void deleteSpecificCartItem(String id);
}
