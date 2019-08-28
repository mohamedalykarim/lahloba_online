package online.lahloba.www.lahloba.ui.delivery;

import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.AppRepository;

public class DeliveryMainViewModel extends ViewModel {
    AppRepository appRepository;

    public DeliveryMainViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }
}
