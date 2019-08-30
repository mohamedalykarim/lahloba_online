package online.lahloba.www.lahloba.ui.delivery_supervisor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import online.lahloba.www.lahloba.data.model.DeliveryArea;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.utils.StatusUtils;

public class DeliverySupervisorMainViewModel extends ViewModel {
    AppRepository appRepository;

    public DeliverySupervisorMainViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetDeliveryAreas() {
        appRepository.startGetDeliveryAreas(StatusUtils.DELIVERY_AREA_TYPE_SUPERVISOR);
    }

    public MutableLiveData<List<DeliveryArea>> getDeliveryAreas() {
        return appRepository.getDeliveryAreas();
    }

    public void startGetOrdersForDeliverysupervisor(String cityId) {
        appRepository.startGetOrdersForDeliverysupervisor(cityId);
    }

    public MutableLiveData<List<OrderItem>> getOrders(){
        return appRepository.getCurrentOrders();
    }

    public void startGetMarketolace(String marketplaceId) {
        appRepository.startGetMarketPlaceForId(marketplaceId);
    }

    public MutableLiveData<MarketPlace> getMarketPlace(){
        return appRepository.getMarketPlace();
    }


    public void startGetDeliveriesForCity(String cityId, int areaType) {
        appRepository.startGetDeliveriesForCity(cityId, areaType);
    }

    public MutableLiveData<List<String>> getDeliveriesId() {
        return appRepository.getDeliveriesId();
    }

    public void startGetUserDetails(String deliverId) {
        appRepository.startGetUserDetails(deliverId);
    }

    public MutableLiveData<UserItem> getUserDetails(){
        return appRepository.getCurrentUserDetails();
    }

    public void clearDeliveriesIdForCity() {
        appRepository.clearDeliveriesIdForCity();
    }

    public void updateOrder(OrderItem orderItem) {
        appRepository.startUpdateOrder(orderItem);
    }
}
