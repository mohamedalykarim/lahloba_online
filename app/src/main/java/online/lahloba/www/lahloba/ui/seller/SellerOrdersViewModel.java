package online.lahloba.www.lahloba.ui.seller;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerOrdersViewModel extends ViewModel {
    SellerRepository repository;

    public SellerOrdersViewModel(SellerRepository repository) {
        this.repository = repository;
    }

    public void startGetSellerOrders() {
        repository.startGetSellerOrders(FirebaseAuth.getInstance().getUid());
    }

}
