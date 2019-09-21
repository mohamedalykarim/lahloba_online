package online.lahloba.www.lahloba.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import online.lahloba.www.lahloba.BR;

public class UserItem extends BaseObservable implements Parcelable {
    String id;
    String firstName;
    String lastName;
    String mobile;
    String email;
    boolean seller;
    boolean delivery;
    boolean deliverySupervisor;
    boolean status;
    double lat;
    double lan;
    int points;
    String notificationToken;


    public UserItem() {
    }

    protected UserItem(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        mobile = in.readString();
        email = in.readString();
        seller = in.readByte() != 0;
        delivery = in.readByte() != 0;
        deliverySupervisor = in.readByte() != 0;
        status = in.readByte() != 0;
        lat = in.readDouble();
        lan = in.readDouble();
        points = in.readInt();
        notificationToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeByte((byte) (seller ? 1 : 0));
        dest.writeByte((byte) (delivery ? 1 : 0));
        dest.writeByte((byte) (deliverySupervisor ? 1 : 0));
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeDouble(lat);
        dest.writeDouble(lan);
        dest.writeInt(points);
        dest.writeString(notificationToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserItem> CREATOR = new Creator<UserItem>() {
        @Override
        public UserItem createFromParcel(Parcel in) {
            return new UserItem(in);
        }

        @Override
        public UserItem[] newArray(int size) {
            return new UserItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }


    @Bindable
    public String getLastName() {
        return lastName;
    }

    @Bindable
    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSeller() {
        return seller;
    }

    public void setSeller(boolean seller) {
        this.seller = seller;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public boolean isDeliverySupervisor() {
        return deliverySupervisor;
    }

    public void setDeliverySupervisor(boolean deliverySupervisor) {
        this.deliverySupervisor = deliverySupervisor;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int point) {
        this.points = point;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }
}
