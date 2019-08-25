package online.lahloba.www.lahloba.ui.order;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.vm_helper.OrderDetailsHelper;
import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.OrderItem;

public class OrderDetailsViewModel extends ViewModel {
    AppRepository appRepository;
    public OrderDetailsHelper helper;

    public OrderDetailsViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        helper = new OrderDetailsHelper();
    }

    public void startReorder(OrderItem orderItem) {
        appRepository.startReorder(orderItem);
    }

    public void startChangeOrderStatus(String orderId, int orderStatus) {
        appRepository.startChangeOrderStatus(orderId, orderStatus);
    }

    public void startGetUserForOrder(String userId) {
        appRepository.startGetUserForOrder(userId);
    }

    public MutableLiveData<UserItem> getUserForOrder() {
        return appRepository.getUserForOrder();
    }

    public void startGetMarketPlaceForOrder(String marketPlaceId) {
        appRepository.startGetMarketPlaceForId(marketPlaceId);
    }

    public MutableLiveData<MarketPlace> getMarketplace(){
        return appRepository.getMarketPlace();
    }

    public void startGetOrderById(String orderId) {
        appRepository.startGetOrderById(orderId);
    }

    public MutableLiveData<OrderItem> getOrderItem(){
        return appRepository.getOrderItem();
    }
}
