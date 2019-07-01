package online.lahloba.www.lahloba.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.Exclude;

import online.lahloba.www.lahloba.BR;


public class ProductItem extends BaseObservable {
    private String id;
    private String image;
    private String title;
    private String description;
    private String price;
    private Object images;
    private String parentId;
    private String marketPlaceId;
    private boolean status;
    private String sellerId;

    @Exclude
    private int count;


    @Exclude
    private String currency;
    @Exclude
    private boolean isFavorite;

    public String getId() {
        return id;
    }

    @Bindable
    public String getImage() {
        return image;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public Object getImages() {
        return images;
    }

    public String getParentId() {
        return parentId;
    }

    @Exclude
    @Bindable
    public String getCurrency() {
        return currency;
    }

    @Exclude
    @Bindable
    public int getCount() {
        return count;
    }

    @Exclude
    @Bindable
    public boolean isFavorite() {
        return isFavorite;
    }

    @Bindable
    public boolean isStatus() {
        return status;
    }

    public String getMarketPlaceId() {
        return marketPlaceId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    public void setImages(Object images) {
        this.images = images;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Exclude
    public void setCurrency(String currency) {
        this.currency = currency;
        notifyPropertyChanged(BR.currency);
    }

    @Exclude
    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    @Exclude
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
        notifyPropertyChanged(BR.favorite);
    }

    public void setStatus(boolean status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    public void setMarketPlaceId(String marketPlaceId) {
        this.marketPlaceId = marketPlaceId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
