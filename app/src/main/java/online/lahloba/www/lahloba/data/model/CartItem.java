package online.lahloba.www.lahloba.data.model;

public class CartItem {
    String id;
    int count;
    String productId;
    String productName;
    String price;
    String image;
    String currency;
    String marketId;


    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }
}
