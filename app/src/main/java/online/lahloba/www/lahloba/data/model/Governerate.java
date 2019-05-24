package online.lahloba.www.lahloba.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Governerate implements Parcelable {
    private String id;
    private String name;
    double lat;
    double lan;

    public Governerate() {
    }

    protected Governerate(Parcel in) {
        id = in.readString();
        name = in.readString();
        lat = in.readDouble();
        lan = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(lat);
        dest.writeDouble(lan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Governerate> CREATOR = new Creator<Governerate>() {
        @Override
        public Governerate createFromParcel(Parcel in) {
            return new Governerate(in);
        }

        @Override
        public Governerate[] newArray(int size) {
            return new Governerate[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }
}