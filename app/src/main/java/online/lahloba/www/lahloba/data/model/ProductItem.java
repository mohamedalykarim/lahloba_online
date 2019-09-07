package online.lahloba.www.lahloba.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import online.lahloba.www.lahloba.BR;


public class ProductItem extends BaseObservable implements Parcelable {
    private String id;
    private String image;
    private String title;
    private String description;
    private String price;
    private Object images;
    private String parentId;
    private String marketPlaceId;
    private boolean status;
    private boolean wantSaveEdit = false;
    private String sellerId;
    private String parentIdMarketPlaceId;
    private String parentIdSellerId;
    int point;


    @Exclude
    private int count;


    @Exclude
    private String currency;
    @Exclude
    private boolean isFavorite;


    public ProductItem() {
    }

    protected ProductItem(Parcel in) {
        id = in.readString();
        image = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        parentId = in.readString();
        marketPlaceId = in.readString();
        status = in.readByte() != 0;
        wantSaveEdit = in.readByte() != 0;
        sellerId = in.readString();
        count = in.readInt();
        currency = in.readString();
        isFavorite = in.readByte() != 0;
        parentIdMarketPlaceId = in.readString();
        parentIdSellerId = in.readString();
        point = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(parentId);
        dest.writeString(marketPlaceId);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeByte((byte) (wantSaveEdit ? 1 : 0));
        dest.writeString(sellerId);
        dest.writeInt(count);
        dest.writeString(currency);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeString(parentIdMarketPlaceId);
        dest.writeString(parentIdSellerId);
        dest.writeInt(point);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

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


    @Exclude
    @Bindable
    public boolean isWantSaveEdit() {
        return wantSaveEdit;
    }

    @Exclude
    @Bindable
    public void setWantSaveEdit(boolean wantSaveEdit) {
        this.wantSaveEdit = wantSaveEdit;
        notifyPropertyChanged(BR.wantSaveEdit);

    }

    public void setParentIdMarketPlaceId(String parentIdMarketPlaceId) {
        this.parentIdMarketPlaceId = parentIdMarketPlaceId;
    }

    public String getParentIdMarketPlaceId() {
        return parentIdMarketPlaceId;
    }

    public String getParentIdSellerId() {
        return parentIdSellerId;
    }

    public void setParentIdSellerId(String parentIdSellerId) {
        this.parentIdSellerId = parentIdSellerId;
    }


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
