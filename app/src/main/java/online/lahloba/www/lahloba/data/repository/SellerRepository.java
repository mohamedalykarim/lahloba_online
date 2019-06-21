package online.lahloba.www.lahloba.data.repository;


import android.arch.lifecycle.MutableLiveData;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
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


    public void startGetSellerOrders(String uid) {
        networkDataHelper.startGetSellerOrders(uid);
    }
}
