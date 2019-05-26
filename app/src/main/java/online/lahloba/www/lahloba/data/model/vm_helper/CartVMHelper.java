package online.lahloba.www.lahloba.data.model.vm_helper;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import online.lahloba.www.lahloba.BR;
import online.lahloba.www.lahloba.data.model.AddressItem;

public class CartVMHelper extends BaseObservable {
    public static final String FREE_SHIPPING = "free_shipping";
    public static final String HYPERLOCAL_SHIPPING = "hyperlocal_shipping";

    private String total;
    private AddressItem addressSelected;
    private String shippingMethodSelected;
    private double hyperlocalCost = 0;

    @Bindable
    public String getTotal() {
        return total;
    }

    @Bindable
    public String getShippingMethodSelected() {
        return shippingMethodSelected;
    }

    @Bindable
    public double getHyperlocalCost() {
        return hyperlocalCost;
    }

    @Bindable
    public AddressItem getAddressSelected() {
        return addressSelected;
    }

    public void setTotal(String total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }


    public void setShippingMethodSelected(String shippingMethodSelected) {
        this.shippingMethodSelected = shippingMethodSelected;
        notifyPropertyChanged(BR.shippingMethodSelected);
    }

    public void setHyperlocalCost(double hyperlocalCost) {
        this.hyperlocalCost = hyperlocalCost;
        notifyPropertyChanged(BR.hyperlocalCost);
    }

    public void setAddressSelected(AddressItem addressSelected) {
        this.addressSelected = addressSelected;
        notifyPropertyChanged(BR.addressSelected);
    }
}
