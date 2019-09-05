package online.lahloba.www.lahloba.ui.favorites;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.FavoriteItem;
import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.ProductItem;

public class FavoritesViewModel extends ViewModel {
    AppRepository appRepository;

    public FavoritesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetFavoriteItems() {
        appRepository.startGetFavoriteItems();
    }

    public MutableLiveData<List<FavoriteItem>> getFavoritesItems() {
        return appRepository.getFavoritesItems();
    }

    public void startGetProductById(String productId) {
        appRepository.startGetProductById(productId);
    }

    public MutableLiveData<ProductItem> getProductItem() {
        return appRepository.getProductItem();
    }
}
