package online.lahloba.www.lahloba.ui.cart;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;

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

    public LiveData<List<CartItemRoom>> getCartItemsFromInternal() {
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

    public LiveData<List<MarketPlace>> getMarketPlaceFromInternal(List<String> ids) {
        return appRepository.getMarketPlaceFromInternal(ids);
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
}
