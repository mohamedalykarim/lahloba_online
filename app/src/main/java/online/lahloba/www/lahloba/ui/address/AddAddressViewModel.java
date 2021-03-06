package online.lahloba.www.lahloba.ui.address;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.GovernorateItem;
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

    public void startGetCitities() {
        appRepository.startGetCities();
    }

    public MutableLiveData<List<CityItem>> getCities(){
        return appRepository.getCities();
    }

    public void startGetGovernorates() {
        appRepository.startGetGovernorates();
    }

    public MutableLiveData<List<GovernorateItem>> getGovernorates() {
        return appRepository.getGovernorates();
    }


}
