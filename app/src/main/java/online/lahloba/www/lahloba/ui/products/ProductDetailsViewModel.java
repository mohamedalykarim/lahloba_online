package online.lahloba.www.lahloba.ui.products;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.ProductItem;
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
}