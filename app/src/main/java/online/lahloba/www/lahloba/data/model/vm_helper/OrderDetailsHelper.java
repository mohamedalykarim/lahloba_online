package online.lahloba.www.lahloba.data.model.vm_helper;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import online.lahloba.www.lahloba.BR;
import online.lahloba.www.lahloba.data.model.OrderItem;

public class OrderDetailsHelper extends BaseObservable {
    private boolean isThisSeller;
    private OrderItem orderItem;

    public OrderDetailsHelper() {
    }

    @Bindable
    public boolean isThisSeller() {
        return isThisSeller;
    }

    @Bindable
    public void setThisSeller(boolean thisSeller) {
        isThisSeller = thisSeller;
        notifyPropertyChanged(BR.thisSeller);
    }


    @Bindable
    public OrderItem getOrderItem() {
        return orderItem;
    }

    @Bindable
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        notifyPropertyChanged(BR.orderItem);
    }
}
