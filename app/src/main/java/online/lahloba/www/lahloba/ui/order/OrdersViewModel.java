package online.lahloba.www.lahloba.ui.order;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.OrderItem;

public class OrdersViewModel extends ViewModel {
    AppRepository appRepository;

    public OrdersViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetCurrentOrders() {
        appRepository.startGetCurrentOrders();
    }

    public MutableLiveData<List<OrderItem>> getCurrentOrders(){
        return appRepository.getCurrentOrders();
    }
}
