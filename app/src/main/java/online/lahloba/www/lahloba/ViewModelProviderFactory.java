package online.lahloba.www.lahloba;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.ui.address.AddAddressViewModel;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.main.MainViewModel;
import online.lahloba.www.lahloba.ui.products.ProductsViewModel;
import online.lahloba.www.lahloba.ui.signup.SignupViewModel;
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
        }else if(modelClass.isAssignableFrom(ProductsViewModel.class)){
            return (T) new ProductsViewModel(appRepository, vmpHelper);
        }else if(modelClass.isAssignableFrom(CartViewModel.class)){
            return (T) new CartViewModel(appRepository, vmpHelper);
        }else if(modelClass.isAssignableFrom(LoginViewModel.class)){
            return (T) new LoginViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(SignupViewModel.class)){
            return (T) new SignupViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(AddressViewModel.class)){
            return (T) new AddressViewModel();
        }else if(modelClass.isAssignableFrom(AddAddressViewModel.class)){
            return (T) new AddAddressViewModel(appRepository);
        }


        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
