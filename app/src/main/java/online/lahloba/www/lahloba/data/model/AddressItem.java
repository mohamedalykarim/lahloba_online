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
    private int floor;
    private int flatNumber;
    private boolean isDefault;

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
        floor = in.readInt();
        flatNumber = in.readInt();
        isDefault = in.readByte() != 0;
    }

    @Exclude
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(zone);
        dest.writeString(street);
        dest.writeString(building);
        dest.writeInt(floor);
        dest.writeInt(flatNumber);
        dest.writeByte((byte) (isDefault ? 1 : 0));
    }

    @Exclude
    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    public static final Creator<AddressItem> CREATOR = new Creator<AddressItem>() {
        @Exclude
        @Override
        public AddressItem createFromParcel(Parcel in) {
            return new AddressItem(in);
        }

        @Exclude
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

    public int getFloor() {
        return floor;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public boolean isDefault() {
        return isDefault;
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

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
