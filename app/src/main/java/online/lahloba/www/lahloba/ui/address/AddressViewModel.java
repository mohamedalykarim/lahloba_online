package online.lahloba.www.lahloba.ui.address;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.vm_helper.AddressVMHelper;

public class AddressViewModel extends ViewModel {
    AppRepository appRepository;
    public AddressVMHelper addressVMHelper;

    public AddressViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        addressVMHelper = new AddressVMHelper();
    }

    public void startGetAddrresses(String userId){
        appRepository.startGetAddrresses(userId);
    }

    public MutableLiveData<List<AddressItem>> getAddrresses() {
        return appRepository.getAddressItems();
    }

    public void startGetDefaultAddress(String uid) {
        appRepository.startGetDefaultAddress(uid);

    }

    public MutableLiveData<AddressItem> getDefaultAddress() {
        return appRepository.getDefaultAddress();
    }

    public void startSetDefaultAddress(String id) {
        appRepository.startSetDefaultAddress(id);
    }

    public void startDeleteAddress(String id) {
        appRepository.startDeleteAddress(id);
    }

    public void startEditAddress(AddressItem editedAddress) {
        appRepository.startEditAddress(editedAddress);
    }

    public MutableLiveData<Boolean> getIsAddressEdited() {
        return appRepository.getIsAddressEdited();
    }
}
