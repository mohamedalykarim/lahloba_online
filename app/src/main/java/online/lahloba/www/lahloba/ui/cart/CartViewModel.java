package online.lahloba.www.lahloba.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.FavoriteItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;
import online.lahloba.www.lahloba.utils.Injector;

public class CartViewModel extends ViewModel {
    AppRepository appRepository;
    public CartVMHelper cartVMHelper;



    public CartViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        cartVMHelper = new CartVMHelper();
    }


    public MutableLiveData<List<CartItem>> getCartItems(){
        return appRepository.getCartItems();
    }

    public LiveData<List<CartItem>> getCartItemsFromInternal() {
        return appRepository.getCartItemFromInternal();
    }

    public void startGetMarketPlaceForId(String id) {
        appRepository.startGetMarketPlaceForId(id);
    }

    public MutableLiveData<MarketPlace> getMarketPlace() {
        return appRepository.getMarketPlace();
    }

    public void cleerMarketPlaceForId() {
        appRepository.cleerMarketPlaceForId();
    }

    public MutableLiveData<List<AddressItem>> getAddresses(){
        return appRepository.getAddressItems();
    }

    public void startGetAddress(String uid) {
        appRepository.startGetAddrresses(uid);
    }

    public void insertMarketPlaceToInternal(MarketPlace marketPlace) {
        appRepository.insertMarketPlaceToInternal(marketPlace);
    }

    public LiveData<List<MarketPlace>> getMarketPlacesFromInternal(List<String> ids) {
        return appRepository.getMarketPlacesFromInternal(ids);
    }

    public MarketPlace getMarketPlaceFromInternal(String id) {
        return appRepository.getMarketPlaceFromInternal(id);
    }



    public void startGetCartItems(String uid) {
        appRepository.startGetCartItems(uid);
    }

    public void startNewOrder(OrderItem orderItem) {
        appRepository.startNewOrder(orderItem);
    }

    public void resetIsOrderAdded(boolean isAdded) {
        appRepository.resetIsOrderAdded(isAdded);
    }

    public MutableLiveData<Boolean> getIsOrderAdded() {
        return appRepository.getIsOrderAdded();
    }

    public void deleteCartItems() {
        Injector.getExecuter().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appRepository.deleteAllFromInternalCart();
                appRepository.startDeleteAllFromCart();
            }
        });
    }

    public void startGetFavoriteItem(String productId) {
        appRepository.startGetFavoriteItem(productId);
    }

    public MutableLiveData<FavoriteItem> getFavoritesItem() {
        return appRepository.getFavoritesItem();
    }

    public void startChangeFavoriteStatus(String productId) {
        appRepository.startChangeFavoriteStatus(productId);
    }

    public void startRemoveFromCartProductCount(String productId) {
        appRepository.startRemoveFromCartProductCount(productId, null);
    }

    public void removeFromCartItemCountInternaldb(String productId) {
        appRepository.removeFromCartItemCountInternaldb(productId);
    }

    public void startAddToCartProductCount(String productId) {
        appRepository.startAddToCartProductCount(productId, null);
    }

    public void addToCartItemCountInternaldb(String productId) {
        appRepository.addToCartItemCountInternaldb(productId);
    }

    public LiveData<CartItem> getSpecificCartItemFromInternal(String productId) {
        return appRepository.getSpecificCartItemFromInternal(productId);
    }

    public void startGetProductItem(String productId) {
        appRepository.startGetProductById(productId);
    }

    public MutableLiveData<ProductItem> getProductItem() {
        return appRepository.getProductItem();
    }

}
