package online.lahloba.www.lahloba.data.model.vm_helper;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import online.lahloba.www.lahloba.BR;

public class CartVMHelper extends BaseObservable {
    String Total;

    @Bindable
    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
        notifyPropertyChanged(BR.total);
    }
}
