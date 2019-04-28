package online.lahloba.www.lahloba.data;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.SubMenuItem;

public class AppRepository {
    private static final Object LOCK = new Object();
    private static AppRepository sInstance;
    NetworkDataHelper networkDataHelper;

    public AppRepository(NetworkDataHelper networkDataHelper) {
        this.networkDataHelper = networkDataHelper;
    }

    public static AppRepository getInstance(NetworkDataHelper networkDataHelper){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new AppRepository(networkDataHelper);
            }
        }

        return sInstance;
    }


    //############################### Main Menu Item ############################//

    public void startGetMainMenuItems() {
        networkDataHelper.startGetMainMenuItems();
    }

    public MutableLiveData<List<MainMenuItem>> getMainMenuItems() {
        return networkDataHelper.getMainMenuItems();
    }

    //############################### Sub Menu Item ############################//
    public void startGetSubMenuItems(String subMenuId) {
        networkDataHelper.startGetSupMenuItems(subMenuId);
    }

    public MutableLiveData<List<SubMenuItem>> getSubMenuItems() {
        return networkDataHelper.getSubMenuItems();
    }

    public void clearSupMenu() {
        networkDataHelper.clearSupMenu();
    }

    //############################### Proucts ############################//
    public void startGetSProductItems(String categoryId) {
        networkDataHelper.startGetProductsFromCategory(categoryId);
    }

    public MutableLiveData<List<ProductItem>> getProductsForCategory() {
        return networkDataHelper.getProductsOfCategory();
    }

    //############################### Cart ############################//
    public void startGetCartItems(String userId) {
        networkDataHelper.startGetCartItems(userId);
    }

    public MutableLiveData<List<CartItem>> getCartItems() {
        return networkDataHelper.getCartItems();
    }


    //############################### Cart ############################//
    public void startLogin(String email, String password) {
        networkDataHelper.startLogin(email, password);

    }

    public MutableLiveData<Boolean> getIsLogged(){
        return networkDataHelper.getIsLogged();
    }

    public void startLogout() {
        networkDataHelper.startLogOut();
    }
}
