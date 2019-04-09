package online.lahloba.www.lahloba;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.ui.main.MainViewModel;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    AppRepository appRepository;

    public ViewModelProviderFactory(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(appRepository);
        }


        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
