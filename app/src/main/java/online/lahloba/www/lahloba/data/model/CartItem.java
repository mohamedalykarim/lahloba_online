package online.lahloba.www.lahloba.data.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

import online.lahloba.www.lahloba.BR;

@Entity(tableName = "cart")
public class CartItem extends BaseObservable implements Serializable {
    String id;
    int count;

    @PrimaryKey
    @NonNull
    String productId;
    String productName;
    String price;
    String image;
    String currency;
    String marketId;

    @Exclude
    boolean isFavorite;



    public String getId() {
        return id;
    }

    @Bindable
    public int getCount() {
        return count;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMarketId() {
        return marketId;
    }

    @Exclude
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    @Exclude
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
