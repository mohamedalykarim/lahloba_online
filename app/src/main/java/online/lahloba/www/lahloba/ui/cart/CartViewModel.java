package online.lahloba.www.lahloba.ui.cart;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.data.model.vm_helper.CartVMHelper;

public class CartViewModel extends ViewModel {
    AppRepository appRepository;
    VMPHelper vmpHelper;
    public CartVMHelper cartVMHelper;



    public CartViewModel(AppRepository appRepository, VMPHelper vmpHelper) {
        this.appRepository = appRepository;
        this.vmpHelper = vmpHelper;
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

    public MutableLiveData<List<AddressItem>> getAddresses(String userId){
        return appRepository.getAddressItems();
    }

    public void startGetAddress(String uid) {
        appRepository.startGetAddrresses(uid);
    }
}
