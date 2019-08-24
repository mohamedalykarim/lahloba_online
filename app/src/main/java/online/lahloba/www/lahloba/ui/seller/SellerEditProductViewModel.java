package online.lahloba.www.lahloba.ui.seller;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.vm_helper.SellerEditProductHelper;
import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerEditProductViewModel extends ViewModel {
    SellerRepository repository;
    public SellerEditProductHelper helper;

    public SellerEditProductViewModel(SellerRepository repository) {
        this.repository = repository;
        helper = new SellerEditProductHelper();
    }

    public void startGetProductForEdit(String productId) {
        repository.startGetProductForEdit(productId);
    }

    public MutableLiveData<ProductItem> getEnProductItemForEdit() {
        return repository.getEnProductItemForEdit();
    }

    public MutableLiveData<ProductItem> getArProductItemForEdit() {
        return repository.getArProductItemForEdit();
    }

    public void startGetMarketPlacesForSeller() {
        repository.startGetMarketPlacesBySeller(FirebaseAuth.getInstance().getUid());
    }

    public MutableLiveData<List<MarketPlace>> getMarketPlacesForSeller() {
        return repository.getMarketPlaces();
    }

    public void resetEditPage() {
        repository.resetEditPage();
    }

    public void startAddNewProduct(Bitmap bitmap, ProductItem enProductItem, ProductItem arProductItem) {
        repository.startAddNewProduct(bitmap, enProductItem, arProductItem);
    }
}
