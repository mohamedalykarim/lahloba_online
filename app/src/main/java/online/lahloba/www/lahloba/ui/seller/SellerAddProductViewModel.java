package online.lahloba.www.lahloba.ui.seller;

import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerAddProductViewModel extends ViewModel {
    SellerRepository repository;

    public SellerAddProductViewModel(SellerRepository repository) {
        this.repository = repository;
    }
}
