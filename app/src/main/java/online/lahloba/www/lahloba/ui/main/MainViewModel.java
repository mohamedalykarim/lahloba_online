package online.lahloba.www.lahloba.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.BannerItem;
import online.lahloba.www.lahloba.data.model.MainMenuItem;

public class MainViewModel extends ViewModel {
    AppRepository appRepository;

    public MainViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetMainMenuItems(){
        appRepository.startGetMainMenuItems();
    }

    public MutableLiveData<List<MainMenuItem>> getMainMenuItems(){
        return appRepository.getMainMenuItems();
    }

    public void startGetBanner() {
        appRepository.startGetBanner();
    }

    public MutableLiveData<List<BannerItem>> getBannerItems() {
        return appRepository.getBannerItems();
    }
}
