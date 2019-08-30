package online.lahloba.www.lahloba.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryArea  {
    private String id;
    private String name;
    double lat;
    double lan;
    String parent;
    String userId;
    int deliveryAreaType;
    String uidType;


    public DeliveryArea() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDeliveryAreaType() {
        return deliveryAreaType;
    }

    public void setDeliveryAreaType(int deliveryAreaType) {
        this.deliveryAreaType = deliveryAreaType;
    }

    public String getUidType() {
        return uidType;
    }

    public void setUidType(String uidType) {
        this.uidType = uidType;
    }
}