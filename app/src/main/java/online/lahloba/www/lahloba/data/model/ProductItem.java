package online.lahloba.www.lahloba.data.model;

public class ProductItem {
    private String image;
    private String title;
    private String price;
    private String currency;
    private String count;
    private boolean isFavorite;

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCount() {
        return count;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
