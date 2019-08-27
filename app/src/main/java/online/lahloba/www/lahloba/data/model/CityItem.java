package online.lahloba.www.lahloba.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CityItem implements Parcelable {
    private String id;
    private String name;
    double lat;
    double lan;
    String parent;

    public CityItem() {
    }

    protected CityItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        lat = in.readDouble();
        lan = in.readDouble();
        parent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lan);
        dest.writeString(parent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CityItem> CREATOR = new Creator<CityItem>() {
        @Override
        public CityItem createFromParcel(Parcel in) {
            return new CityItem(in);
        }

        @Override
        public CityItem[] newArray(int size) {
            return new CityItem[size];
        }
    };


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
}