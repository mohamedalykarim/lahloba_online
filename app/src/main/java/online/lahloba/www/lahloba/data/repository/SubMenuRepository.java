package online.lahloba.www.lahloba.data.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
import online.lahloba.www.lahloba.data.model.SubMenuItem;

public class SubMenuRepository {
    private static final Object LOCK = new Object();
    private static SubMenuRepository sInstance;
    NetworkDataHelper networkDataHelper;
    LahlobaDatabase database;

    public SubMenuRepository(NetworkDataHelper networkDataHelper, LahlobaDatabase database) {
        this.networkDataHelper = networkDataHelper;
        this.database = database;
    }

    public static SubMenuRepository getInstance(NetworkDataHelper networkDataHelper, LahlobaDatabase database){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new SubMenuRepository(networkDataHelper, database);
            }
        }

        return sInstance;
    }




    public void startGetSubMenuItems(String subMenuId) {
        networkDataHelper.startGetSupMenuItems(subMenuId);
    }

    public MutableLiveData<List<SubMenuItem>> getSubMenuItems() {
        return networkDataHelper.getSubMenuItems();
    }

    public void clearSupMenu() {
        networkDataHelper.clearSupMenu();
    }

}
