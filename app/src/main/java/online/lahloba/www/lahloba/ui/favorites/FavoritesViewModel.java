package online.lahloba.www.lahloba.ui.favorites;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.ProductItem;

public class FavoritesViewModel extends ViewModel {
    AppRepository appRepository;

    public FavoritesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetFavoriteItems() {
        appRepository.startGetFavoriteItems();
    }

    public MutableLiveData<List<ProductItem>> getFavoritesItems() {
        return appRepository.getFavoritesItems();
    }
}
