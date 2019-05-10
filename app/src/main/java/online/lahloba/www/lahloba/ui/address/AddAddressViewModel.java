package online.lahloba.www.lahloba.ui.address;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.AddressItem;

public class AddAddressViewModel extends ViewModel {
    AppRepository appRepository;

    public AddAddressViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startAddNewAddress(String userId, AddressItem addressItem) {
        appRepository.startAddNewAddress(userId,  addressItem);
    }


    public MutableLiveData<Boolean> getIsAddressAdded(){
        return appRepository.getIsAddressAdded();
    }

    public void setIsAddressAddedFalse() {
        appRepository.setIsAddressAddedFalse();
    }
}
