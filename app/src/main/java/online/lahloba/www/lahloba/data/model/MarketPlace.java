package online.lahloba.www.lahloba.data.model;

public class MarketPlace{
    String id;
    String name;
    String sellerId;
    String sellerName;
    String governerateId;
    double lat;
    double lan;


    public String getId() {
        return id;
    }

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

    public void setName(String name) {
        this.name = name;
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
}
