package online.lahloba.www.lahloba.ui.seller;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerProductsCategoryViewModel extends ViewModel {
    SellerRepository repository;

    public SellerProductsCategoryViewModel(SellerRepository repository) {
        this.repository = repository;
    }

    public void startGetSubMenusWithNoChild() {
        repository.startGetSubMenusWithNoChild();
    }

    public MutableLiveData<List<SubMenuItem>> getSubMenuItems() {
        return repository.getSubMenuItems();
    }
}
