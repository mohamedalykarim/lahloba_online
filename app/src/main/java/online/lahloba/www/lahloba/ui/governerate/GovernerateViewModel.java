package online.lahloba.www.lahloba.ui.governerate;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;

import online.lahloba.www.lahloba.data.AppRepository;

public class GovernerateViewModel extends ViewModel {
    AppRepository appRepository;

    public GovernerateViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startGetGovernorates() {
        appRepository.startGetGovernorates();
    }

    public MutableLiveData<DataSnapshot> getGovernorates() {
        return appRepository.getGovernorates();
    }


}
