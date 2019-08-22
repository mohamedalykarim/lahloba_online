package online.lahloba.www.lahloba.data.model.vm_helper;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import online.lahloba.www.lahloba.BR;

public class ProductVMHelper extends BaseObservable {
    int cartCount = 0;

    @Bindable
    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
        notifyPropertyChanged(BR.cartCount);
    }
}
