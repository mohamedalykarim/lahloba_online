package online.lahloba.www.lahloba.ui.seller;

import android.arch.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerProductsViewModel extends ViewModel {
    SellerRepository repository;

    public SellerProductsViewModel(SellerRepository repository) {
        this.repository = repository;
    }
}
