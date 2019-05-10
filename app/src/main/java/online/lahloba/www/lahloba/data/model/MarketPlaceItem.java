package online.lahloba.www.lahloba.data.model;

public class MarketPlaceItem {
    String id;
    String sellerId;
    String sellerName;
    String name;
    String location;


    public String getId() {
        return id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
