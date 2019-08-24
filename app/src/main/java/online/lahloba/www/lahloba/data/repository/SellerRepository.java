package online.lahloba.www.lahloba.data.repository;


import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.model.UserItem;

public class SellerRepository {
    private static final Object LOCK = new Object();
    private static SellerRepository sInstance;
    NetworkDataHelper networkDataHelper;
    LahlobaDatabase database;

    public SellerRepository(NetworkDataHelper networkDataHelper, LahlobaDatabase database) {
        this.networkDataHelper = networkDataHelper;
        this.database = database;
    }

    public static SellerRepository getInstance(NetworkDataHelper networkDataHelper, LahlobaDatabase database){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new SellerRepository(networkDataHelper, database);
            }
        }

        return sInstance;
    }


    public MutableLiveData<UserItem> getCurrentUserDetails() {
        return networkDataHelper.getCurrentUserDetails();
    }


    public void startGetSellerOrders(String marketId) {
        networkDataHelper.startGetSellerOrders(marketId);
    }

    public void startGetMarketPlacesBySeller(String uid) {
        networkDataHelper.startGetMarketPlacesBySeller(uid);
    }

    public MutableLiveData<List<MarketPlace>> getMarketPlaces() {
        return networkDataHelper.getMarketPlaces();
    }

    public MutableLiveData<List<OrderItem>> getSellerOrder() {
        return networkDataHelper.getCurrentOrders();
    }

    public void startGetSubMenusWithNoChild() {
        networkDataHelper.startGetSubMenusWithNoChild();
    }

    public MutableLiveData<List<SubMenuItem>> getSubMenuItems() {
        return networkDataHelper.getSubMenuItems();
    }

    public void startGetProductForCategoryAndUser(String category) {
        networkDataHelper.startGetProductForCategoryAndUser(category);
    }

    public MutableLiveData<List<ProductItem>> getProducts() {
        return networkDataHelper.getProducts();
    }

    public void startChangeProductStatus(String productId, boolean isEnable) {
        networkDataHelper.startChangeProductStatus(productId, isEnable);
    }

    public void startChangeProductPrice(String productId,String price) {
        networkDataHelper.startChangeProductPrice(productId, price);
    }

    public void startEditProduct(ProductItem productItem, String language) {
        networkDataHelper.startEditProduct(productItem, language);
    }

    public void startAddNewProduct(Bitmap myBitmap, ProductItem enProductItem, ProductItem arProductItem) {
        networkDataHelper.startAddNewProduct(myBitmap, enProductItem, arProductItem);
    }

    public void startGetProductForEdit(String productId) {
        networkDataHelper.startGetProductForEdit(productId);
    }

    public MutableLiveData<ProductItem> getEnProductItemForEdit() {
        return networkDataHelper.getEnProductItemForEdit();
    }

    public MutableLiveData<ProductItem> getArProductItemForEdit() {
        return networkDataHelper.getArProductItemForEdit();
    }

    public void resetEditPage() {
        networkDataHelper.resetEditPage();
    }
}
