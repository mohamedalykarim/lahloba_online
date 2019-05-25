package online.lahloba.www.lahloba.data.model;

public class UserItem {
    String id;
    String firstName;
    String lastName;
    String mobile;
    String email;
    boolean isSeller;
    boolean status;
    String sellerId;
    double lat;
    double lan;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }


    public boolean isStatus() {
        return status;
    }


    public boolean isSeller() {
        return isSeller;
    }

    public String getSellerId() {
        return sellerId;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

}
