package online.lahloba.www.lahloba.data.repository;


import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
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


    public void startGetSellerOrders(String uid, String marketId) {
        networkDataHelper.startGetSellerOrders(uid, marketId);
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
}
