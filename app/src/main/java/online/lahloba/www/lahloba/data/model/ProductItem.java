package online.lahloba.www.lahloba.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.Exclude;

import online.lahloba.www.lahloba.BR;


public class ProductItem extends BaseObservable {
    private String id;
    private String image;
    private String title;
    private String price;
    private Object images;
    private String parentId;

    @Exclude
    private String currency;
    @Exclude
    private int count;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
//        notifyPropertyChanged(BR.image);
    }

    public void setTitle(String title) {
        this.title = title;
//        notifyPropertyChanged(BR.title);
    }

    public void setPrice(String price) {
        this.price = price;
//        notifyPropertyChanged(BR.price);
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
}
