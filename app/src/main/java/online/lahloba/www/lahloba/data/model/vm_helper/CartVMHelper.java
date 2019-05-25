package online.lahloba.www.lahloba.data.model.vm_helper;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import online.lahloba.www.lahloba.BR;

public class CartVMHelper extends BaseObservable {
    public static final String FREE_SHIPPING = "free_shipping";
    public static final String HYPERLOCAL_SHIPPING = "hyperlocal_shipping";

    private String total;
    private String shippingMethodSelected;

    @Bindable
    public String getTotal() {
        return total;
    }

    @Bindable
    public String getShippingMethodSelected() {
        return shippingMethodSelected;
    }


    public void setTotal(String total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }


    public void setShippingMethodSelected(String shippingMethodSelected) {
        this.shippingMethodSelected = shippingMethodSelected;
        notifyPropertyChanged(BR.shippingMethodSelected);
    }
}
