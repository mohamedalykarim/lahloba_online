package online.lahloba.www.lahloba.ui.order;

import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.OrderItem;

public class OrderDetailsViewModel extends ViewModel {
    AppRepository appRepository;

    public OrderDetailsViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startReorder(OrderItem orderItem) {
        appRepository.startReorder(orderItem);
    }

    public void startChangeOrderStatus(String orderId, int orderStatus) {
        appRepository.startChangeOrderStatus(orderId, orderStatus);
    }
}
