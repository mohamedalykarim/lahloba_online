package online.lahloba.www.lahloba.data.model.vm_helper;

import androidx.databinding.BaseObservable;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.FavoriteItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.UserItem;

public class ProductDetailsHelper extends BaseObservable {
    ProductItem productItem;
    MarketPlace marketPlace;
    UserItem sellerItem;
    CartItem cartItem;
    FavoriteItem favoriteItem;
    double addedPrice;


    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public MarketPlace getMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    public UserItem getSellerItem() {
        return sellerItem;
    }

    public void setSellerItem(UserItem sellerItem) {
        this.sellerItem = sellerItem;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public FavoriteItem getFavoriteItem() {
        return favoriteItem;
    }

    public void setFavoriteItem(FavoriteItem favoriteItem) {
        this.favoriteItem = favoriteItem;
    }

    public double getAddedPrice() {
        return addedPrice;
    }

    public void setAddedPrice(double addedPrice) {
        this.addedPrice = addedPrice;
    }
}
