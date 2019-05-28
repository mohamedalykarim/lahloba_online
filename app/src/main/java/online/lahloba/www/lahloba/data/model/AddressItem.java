package online.lahloba.www.lahloba.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class AddressItem implements Parcelable {
    private String id;
    private String name;
    private String country;
    private String city;
    private String zone;
    private String street;
    private String building;
    private double lat;
    private double lon;
    private int floor;
    private int flatNumber;
    private boolean isDefault;
    private String address;

    public AddressItem() {
    }

    protected AddressItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        country = in.readString();
        city = in.readString();
        zone = in.readString();
        street = in.readString();
        building = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        floor = in.readInt();
        flatNumber = in.readInt();
        isDefault = in.readByte() != 0;
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(zone);
        dest.writeString(street);
        dest.writeString(building);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeInt(floor);
        dest.writeInt(flatNumber);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressItem> CREATOR = new Creator<AddressItem>() {
        @Override
        public AddressItem createFromParcel(Parcel in) {
            return new AddressItem(in);
        }

        @Override
        public AddressItem[] newArray(int size) {
            return new AddressItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getZone() {
        return zone;
    }

    public String getStreet() {
        return street;
    }

    public String getBuilding() {
        return building;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getFloor() {
        return floor;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
