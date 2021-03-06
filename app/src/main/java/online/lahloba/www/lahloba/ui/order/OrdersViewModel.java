package online.lahloba.www.lahloba.ui.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.repository.AppRepository;
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

    public void startRemoveOrder(String orderId) {
        appRepository.startRemoveOrder(orderId);
    }

    public void startReorder(OrderItem orderItem) {
        appRepository.startReorder(orderItem);
    }
}
