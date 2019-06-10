package online.lahloba.www.lahloba.data.model.vm_helper;

import android.util.Log;

import com.google.firebase.database.Exclude;

import online.lahloba.www.lahloba.data.model.AddressItem;

public class AddressVMHelper {
    AddressItem editedAddress;

    public AddressVMHelper() {
        editedAddress = new AddressItem();
    }

    public AddressItem getEditedAddress() {
        return editedAddress;
    }

    public void setEditedAddress(AddressItem editedAddress) {
        this.editedAddress = editedAddress;
    }


    @Exclude
    public void onEditedAddressCheckedChanged(boolean checked){
        editedAddress.setDefaultAddress(checked);
    }
}
