package online.lahloba.www.lahloba.ui.order;

import android.arch.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.AppRepository;

public class OrdersViewModel extends ViewModel {
    AppRepository appRepository;

    public OrdersViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

}
