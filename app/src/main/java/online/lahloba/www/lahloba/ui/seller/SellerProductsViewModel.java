package online.lahloba.www.lahloba.ui.seller;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerProductsViewModel extends ViewModel {
    SellerRepository sellerRepository;

    public SellerProductsViewModel(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public void startGetProductForCategoryAndUser(String category) {
        sellerRepository.startGetProductForCategoryAndUser(category);
    }

    public MutableLiveData<List<ProductItem>> getProducts() {
        return sellerRepository.getProducts();
    }

    public void startChangeProductStatus(String productId, boolean isEnable) {
        sellerRepository.startChangeProductStatus(productId, isEnable);
    }

    public void startChangeProductPrice(String productId, String price) {
        sellerRepository.startChangeProductPrice(productId, price);
    }

    public void onSwitchCheckedChanged(String productId, boolean isWork){
        startChangeProductStatus(productId, isWork);
    }

    public void startEditProduct(ProductItem productItem, String language) {
        sellerRepository.startEditProduct(productItem, language);
    }
}
