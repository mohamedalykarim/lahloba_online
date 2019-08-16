package online.lahloba.www.lahloba;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.repository.SellerRepository;
import online.lahloba.www.lahloba.data.repository.SubMenuRepository;
import online.lahloba.www.lahloba.ui.address.AddAddressViewModel;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;
import online.lahloba.www.lahloba.ui.cart.CartViewModel;
import online.lahloba.www.lahloba.ui.favorites.FavoritesViewModel;
import online.lahloba.www.lahloba.ui.governerate.GovernerateViewModel;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.ui.main.MainViewModel;
import online.lahloba.www.lahloba.ui.news.NewsViewModel;
import online.lahloba.www.lahloba.ui.order.OrderDetailsViewModel;
import online.lahloba.www.lahloba.ui.order.OrdersViewModel;
import online.lahloba.www.lahloba.ui.products.ProductsViewModel;
import online.lahloba.www.lahloba.ui.seller.SellerProductsCategoryViewModel;
import online.lahloba.www.lahloba.ui.seller.SellerOrdersViewModel;
import online.lahloba.www.lahloba.ui.seller.SellerProductsViewModel;
import online.lahloba.www.lahloba.ui.signup.SignupViewModel;
import online.lahloba.www.lahloba.ui.sub_menu.SubMenuViewModel;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {
    AppRepository appRepository;
    SubMenuRepository subMenuRepository;
    SellerRepository sellerRepository;


    public ViewModelProviderFactory(AppRepository appRepository, SubMenuRepository subMenuRepository, SellerRepository sellerRepository) {
        this.appRepository = appRepository;
        this.subMenuRepository = subMenuRepository;
        this.sellerRepository = sellerRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(SubMenuViewModel.class)){
            return (T) new SubMenuViewModel(subMenuRepository);
        }else if(modelClass.isAssignableFrom(ProductsViewModel.class)){
            return (T) new ProductsViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(CartViewModel.class)){
            return (T) new CartViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(LoginViewModel.class)){
            return (T) new LoginViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(SignupViewModel.class)){
            return (T) new SignupViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(AddressViewModel.class)){
            return (T) new AddressViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(AddAddressViewModel.class)){
            return (T) new AddAddressViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(GovernerateViewModel.class)){
            return (T) new GovernerateViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(OrdersViewModel.class)){
            return (T) new OrdersViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(FavoritesViewModel.class)){
            return (T) new FavoritesViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(NewsViewModel.class)){
            return (T) new NewsViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(OrderDetailsViewModel.class)){
            return (T) new OrderDetailsViewModel(appRepository);
        }else if(modelClass.isAssignableFrom(SellerProductsCategoryViewModel.class)){
            return (T) new SellerProductsCategoryViewModel(sellerRepository);
        }else if(modelClass.isAssignableFrom(SellerProductsViewModel.class)){
            return (T) new SellerProductsViewModel(sellerRepository);
        }else if(modelClass.isAssignableFrom(SellerOrdersViewModel.class)){
            return (T) new SellerOrdersViewModel(sellerRepository);
        }


        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
