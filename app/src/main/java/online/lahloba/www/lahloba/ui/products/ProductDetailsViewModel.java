package online.lahloba.www.lahloba.ui.products;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.FavoriteItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.ProductOption;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.ProductDetailsHelper;
import online.lahloba.www.lahloba.data.repository.AppRepository;

public class ProductDetailsViewModel extends ViewModel {
    AppRepository appRepository;
    public ProductDetailsHelper helper;

    public ProductDetailsViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        helper = new ProductDetailsHelper();
    }

    public void startGetProductItem(String productId) {
        appRepository.startGetProductById(productId);
    }

    public MutableLiveData<ProductItem> getProduct() {
        return appRepository.getProductItem();
    }

    public void startGetMarketPlace(String marketPlaceId) {
        appRepository.startGetMarketPlaceForId(marketPlaceId);
    }


    public MutableLiveData<MarketPlace> getMarketPlace() {
        return appRepository.getMarketPlace();
    }

    public void startGetSellerDetails(String sellerId) {
        appRepository.startGetUserDetails(sellerId);
    }


    public MutableLiveData<UserItem> getUserDetails(){
        return appRepository.getCurrentUserDetails();
    }

    public void startGetCartItemForProduct(String productId) {
        appRepository.startGetCartItemById(productId);
    }

    public MutableLiveData<CartItem> getCartItem(){
        return appRepository.getCartItem();
    }

    public void startAddItemToCart(ProductItem productItem, HashMap<String, ProductOption> productOptions) {
        appRepository.startAddProductToFirebaseCart(productItem, productOptions);
    }

    public void startAddtoCartCount(String productId, HashMap<String, ProductOption> productOptions) {
        appRepository.startAddToCartProductCount(productId, productOptions);
    }

    public void startRemoveFromCartCount(String productId, HashMap<String, ProductOption> optionHashMap) {
        appRepository.startRemoveFromCartProductCount(productId, optionHashMap);
    }

    public void startChangeFavoriteStatus(String productId){
        appRepository.startChangeFavoriteStatus(productId);
    }

    public MutableLiveData<FavoriteItem> getFavoritesItem(){
        return appRepository.getFavoritesItem();
    }

    public void startGetFavoriteItem(String productId) {
        appRepository.startGetFavoriteItem(productId);
    }

    public void startGetProductOptions(String productId) {
        appRepository.startGetProductOptions(productId);
    }

    public MutableLiveData<DataSnapshot> getProductOptions() {
        return appRepository.getProductOptions();
    }

    public void startAddOptionToCartItem(String productId, ProductOption productOption) {
        appRepository.startAddOptionToCartItem(productId, productOption);
    }

    public void startResetProductOptions() {
        appRepository.startResetProductOptions();
    }
}
