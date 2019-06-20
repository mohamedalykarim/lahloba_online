package online.lahloba.www.lahloba.ui.seller;

import android.arch.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerMainViewModel extends ViewModel {
    SellerRepository repository;

    public SellerMainViewModel(SellerRepository repository) {
        this.repository = repository;
    }
}
