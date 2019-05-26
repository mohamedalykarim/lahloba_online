package online.lahloba.www.lahloba.data.model.room_entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cart")
public class CartItemRoom {
    int count;
    @PrimaryKey
    @NonNull
    String productId;
    String productName;
    String price;
    String image;
    String currency;
    String marketId;



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

    public void setCount(int count) {
        this.count = count;
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
}
