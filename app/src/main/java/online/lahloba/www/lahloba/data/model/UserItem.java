package online.lahloba.www.lahloba.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import online.lahloba.www.lahloba.BR;

public class UserItem extends BaseObservable {
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


    public UserItem() {
    }

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
}
