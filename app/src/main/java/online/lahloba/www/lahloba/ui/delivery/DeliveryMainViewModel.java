package online.lahloba.www.lahloba.ui.delivery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.repository.AppRepository;

public class DeliveryMainViewModel extends ViewModel {
    AppRepository appRepository;

    public DeliveryMainViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetMarketplaceForOrder(String marketplaceId) {
        appRepository.startGetMarketPlaceForId(marketplaceId);
    }

    public MutableLiveData<MarketPlace> getMarketPlace(){
        return appRepository.getMarketPlace();
    }

    public void startGetOrdersForDelivery() {
        appRepository.startGetOrdersForDelivery();
    }

    public MutableLiveData<List<OrderItem>> getOrders(){
        return appRepository.getCurrentOrders();
    }
}
