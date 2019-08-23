package online.lahloba.www.lahloba.ui.seller;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.vm_helper.SellerAddProductHelper;
import online.lahloba.www.lahloba.data.repository.SellerRepository;

public class SellerAddProductViewModel extends ViewModel {
    SellerRepository repository;
    public SellerAddProductHelper helper;


    public SellerAddProductViewModel(SellerRepository repository) {
        this.repository = repository;
        helper = new SellerAddProductHelper();

    }


    public void startGetMarketPlacesForSeller() {
        repository.startGetMarketPlacesBySeller(FirebaseAuth.getInstance().getUid());
    }

    public MutableLiveData<List<MarketPlace>> getMarketPlacesForSeller() {
        return repository.getMarketPlaces();
    }

    public void startAddNewProduct(Bitmap myBitmap, ProductItem enProductItem, ProductItem arProductItem) {
        repository.startAddNewProduct(myBitmap, enProductItem, arProductItem);
    }
}
