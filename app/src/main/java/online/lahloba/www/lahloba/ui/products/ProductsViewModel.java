package online.lahloba.www.lahloba.ui.products;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.AppRepository;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.data.model.vm_helper.ProductVMHelper;

public class ProductsViewModel extends ViewModel {
    AppRepository appRepository;
    String categoryId;
    public ProductVMHelper productVMHelper = new ProductVMHelper();

    public ProductsViewModel(AppRepository appRepository, VMPHelper vmpHelper) {
        this.appRepository = appRepository;
        categoryId = vmpHelper.getCategoryId();
        this.categoryId = categoryId;
    }

    public void startProductsForCategory() {
        appRepository.startGetSProductItems(categoryId);
    }

    public MutableLiveData<List<ProductItem>> getProductsForCategory() {
        return appRepository.getProductsForCategory();
    }

    public MutableLiveData<List<CartItem>> getCartItem() {
        return appRepository.getCartItems();
    }

    public void startGetCartItems(String userId){
        appRepository.startGetCartItems(userId);
    }
}
