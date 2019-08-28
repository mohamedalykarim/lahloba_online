package online.lahloba.www.lahloba.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import online.lahloba.www.lahloba.BR;

public class AddressItem extends BaseObservable implements Parcelable {
    private String id;
    private String name;
    private String country;
    private String city;
    private String cityId;
    private String governorate;
    private String governorateId;
    private String zone;
    private String street;
    private String building;
    private double lat;
    private double lon;
    private int floor;
    private int flatNumber;
    private boolean isDefaultAddress;
    private String address;

    public AddressItem() {
    }

    protected AddressItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        country = in.readString();
        city = in.readString();
        cityId = in.readString();
        governorate = in.readString();
        governorateId = in.readString();
        zone = in.readString();
        street = in.readString();
        building = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        floor = in.readInt();
        flatNumber = in.readInt();
        isDefaultAddress = in.readByte() != 0;
        address = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(cityId);
        dest.writeString(governorate);
        dest.writeString(governorateId);
        dest.writeString(zone);
        dest.writeString(street);
        dest.writeString(building);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeInt(floor);
        dest.writeInt(flatNumber);
        dest.writeByte((byte) (isDefaultAddress ? 1 : 0));
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

    @Bindable
    public boolean isDefaultAddress() {
        return isDefaultAddress;
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


    @Bindable
    public void setDefaultAddress(boolean aDefault) {
        isDefaultAddress = aDefault;
        notifyPropertyChanged(BR.defaultAddress);
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getGovernorateId() {
        return governorateId;
    }

    public void setGovernorateId(String governorateId) {
        this.governorateId = governorateId;
    }
}
