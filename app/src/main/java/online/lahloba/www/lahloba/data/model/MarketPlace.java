package online.lahloba.www.lahloba.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.annotation.NonNull;
import androidx.room.TypeConverters;

import com.google.firebase.database.Exclude;

import online.lahloba.www.lahloba.BR;

@Entity(tableName = "market_places")
public class MarketPlace extends BaseObservable {
    @NonNull
    @PrimaryKey
    String id;
    String name;
    String sellerId;
    String sellerName;
    String governerateId;
    double lat;
    double lan;

    private AddressItem addressSelected;



    public String getId() {
        return id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getGovernerateId() {
        return governerateId;
    }

    public double getLat() {
        return lat;
    }

    public double getLan() {
        return lan;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setGovernerateId(String governerateId) {
        this.governerateId = governerateId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    @TypeConverters(AddressItem.class)
    public AddressItem getAddressSelected() {
        return addressSelected;
    }

    public void setAddressSelected(AddressItem addressSelected) {
        this.addressSelected = addressSelected;
    }
}
