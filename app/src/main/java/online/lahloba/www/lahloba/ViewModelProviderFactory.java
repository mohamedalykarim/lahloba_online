package online.lahloba.www.lahloba;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.ui.main.MainViewModel;
import online.lahloba.www.lahloba.ui.sub_menu.SubMenuViewModel;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    AppRepository appRepository;
    VMPHelper vmpHelper;

    public ViewModelProviderFactory(AppRepository appRepository, VMPHelper vmpHelper) {
        this.appRepository = appRepository;
        this.vmpHelper = vmpHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(SubMenuViewModel.class)){
            return (T) new SubMenuViewModel(appRepository, vmpHelper);
        }


        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
