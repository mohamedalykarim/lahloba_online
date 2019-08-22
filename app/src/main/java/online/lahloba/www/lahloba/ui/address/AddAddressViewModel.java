package online.lahloba.www.lahloba.ui.address;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.AppRepository;
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
