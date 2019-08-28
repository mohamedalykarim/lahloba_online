package online.lahloba.www.lahloba.ui.city;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.GovernorateItem;
import online.lahloba.www.lahloba.data.repository.AppRepository;

public class CityViewModel extends ViewModel {
    AppRepository appRepository;

    public CityViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetCities() {
        appRepository.startGetCities();
    }

    public MutableLiveData<List<CityItem>> getCities() {
        return appRepository.getCities();
    }


    public void startGetGovernorate() {
        appRepository.startGetGovernorates();
    }

    public MutableLiveData<List<GovernorateItem>> getGovernorates() {
        return appRepository.getGovernorates();
    }


}
