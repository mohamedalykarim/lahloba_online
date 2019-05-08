package online.lahloba.www.lahloba.ui.address;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.AppRepository;

public class AddAddressViewModel extends ViewModel {
    AppRepository appRepository;

    public AddAddressViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startAddNewAddress(String userId, String name, String country, String city, String zone,
                                   String street, String building, int floor, int flat) {
        appRepository.startAddNewAddress(userId, name, country, city, zone, street, building, floor, flat);
    }


    public MutableLiveData<Boolean> getIsAddressAdded(){
        return appRepository.getIsAddressAdded();
    }

    public void setIsAddressAddedFalse() {
        appRepository.setIsAddressAddedFalse();
    }
}
