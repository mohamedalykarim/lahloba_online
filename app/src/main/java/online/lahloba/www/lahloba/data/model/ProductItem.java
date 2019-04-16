package online.lahloba.www.lahloba.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import online.lahloba.www.lahloba.BR;

public class ProductItem extends BaseObservable {
    private String id;
    private String image;
    private String title;
    private String price;
    private String currency;
    private int count;
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

    @Bindable
    public String getCurrency() {
        return currency;
    }

    @Bindable
    public int getCount() {
        return count;
    }

    @Bindable
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        notifyPropertyChanged(BR.currency);
    }

    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
        notifyPropertyChanged(BR.favorite);
    }
}
