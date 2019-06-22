package online.lahloba.www.lahloba.ui.seller;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerOrdersViewModel extends ViewModel {
    SellerRepository repository;

    public SellerOrdersViewModel(SellerRepository repository) {
        this.repository = repository;
    }

    public void startGetSellerOrders(String marketId) {
        repository.startGetSellerOrders(FirebaseAuth.getInstance().getUid(), marketId);
    }

    public void startGetMarketPlacesBySeller() {
        repository.startGetMarketPlacesBySeller(FirebaseAuth.getInstance().getUid());
    }

    public MutableLiveData<List<MarketPlace>> getMarketPlaces() {
        return repository.getMarketPlaces();
    }

    public MutableLiveData<List<OrderItem>> getSellerOrder() {
        return repository.getSellerOrder();
    }
}
