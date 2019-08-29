package online.lahloba.www.lahloba.ui.delivery_supervisor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.repository.AppRepository;

public class DeliverySupervisorMainViewModel extends ViewModel {
    AppRepository appRepository;

    public DeliverySupervisorMainViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetDeliveryArea() {
        appRepository.startGetDeliveryArea();
    }

    public MutableLiveData<List<CityItem>> getDeliveryAreas() {
        return appRepository.getDeliveryAreas();
    }

    public void startGetOrdersForDeliverysupervisor(String cityId) {
        appRepository.startGetOrdersForDeliverysupervisor(cityId);
    }

    public MutableLiveData<List<OrderItem>> getOrders(){
        return appRepository.getCurrentOrders();
    }
}
