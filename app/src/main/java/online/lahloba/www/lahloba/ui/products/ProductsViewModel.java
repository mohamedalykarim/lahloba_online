package online.lahloba.www.lahloba.ui.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.repository.AppRepository;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.vm_helper.ProductVMHelper;
import online.lahloba.www.lahloba.utils.Injector;

public class ProductsViewModel extends ViewModel {
    AppRepository appRepository;
    public ProductVMHelper productVMHelper = new ProductVMHelper();

    public ProductsViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void startProductsForCategory(String categoryId) {
        appRepository.startGetSProductItems(categoryId);
    }

    public MutableLiveData<List<ProductItem>> getProductsForCategory() {
        return appRepository.getProductsForCategory();
    }

    public MutableLiveData<List<CartItem>> getCartItems() {
        return appRepository.getCartItems();
    }

    public void startGetCartItems(String userId){
            appRepository.startGetCartItems(userId);

    }

    public LiveData<List<CartItem>> getCartItemFromInternal() {
        return appRepository.getCartItemFromInternal();
    }


    public void startResetFirebaseCart() {
        appRepository.startResetFirebaseCart();
    }

    public void startAddProductToFirebaseCart(ProductItem productItem) {
        appRepository.startAddProductToFirebaseCart(productItem);
    }

    public void startGetCartItemById(String productId) {
        appRepository.startGetCartItemById(productId);
    }

    public MutableLiveData<CartItem> getCartItem() {
        return appRepository.getCartItem();
    }

    public void startAddToCartProductCount(String productId) {
        appRepository.startAddToCartProductCount(productId);
    }

    public void startRemoveFromCartProductCount(String productId) {
        appRepository.startRemoveFromCartProductCount(productId);
    }

    public void insertCartItemToInternaldb(CartItem cartItem) {
        appRepository.insertCartItemToInternaldb(cartItem);
    }

    public LiveData<CartItem> getSpecificCartItemFromInternal(String productId) {
        return appRepository.getSpecificCartItemFromInternal(productId);
    }

    public void addToCartItemCountInternaldb(String productId) {
        appRepository.addToCartItemCountInternaldb(productId);
    }

    public void removeFromCartItemCountInternaldb(String productId) {
        appRepository.removeFromCartItemCountInternaldb(productId);
    }

    public void removeCartitemWith0CountFromInternal() {
        appRepository.removeCartitemWith0CountFromInternal();
    }
}
