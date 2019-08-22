package online.lahloba.www.lahloba.ui.news;

import androidx.lifecycle.ViewModel;

import online.lahloba.www.lahloba.data.repository.AppRepository;

public class NewsViewModel extends ViewModel {
    AppRepository appRepository;

    public NewsViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }
}
