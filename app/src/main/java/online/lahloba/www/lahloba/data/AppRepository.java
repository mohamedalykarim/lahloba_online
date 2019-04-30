package online.lahloba.www.lahloba.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.utils.Injector;

public class AppRepository {
    private static final Object LOCK = new Object();
    private static AppRepository sInstance;
    NetworkDataHelper networkDataHelper;
    LahlobaDatabase database;

    public AppRepository(NetworkDataHelper networkDataHelper, LahlobaDatabase database) {
        this.networkDataHelper = networkDataHelper;
        this.database = database;
    }

    public static AppRepository getInstance(NetworkDataHelper networkDataHelper, LahlobaDatabase database){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new AppRepository(networkDataHelper, database);
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

    public LiveData<List<CartItemRoom>> startgetCartItemsFromInternal() {
        return database.cartDao().getAll();
    }

    public void insertCartItemToInternaldb(CartItemRoom cartItem){
        database.cartDao().insert(cartItem);
    }

    public void changeCartItemCountInternaldb(String productId, int count){
        database.cartDao().changeCount(productId,count);
    }

    public LiveData<CartItemRoom> getSpecificCartItem(String productId) {
        return database.cartDao().getSpecificCartItem(productId);
    }

    public void deleteSpecificCartItem(String productId) {
        database.cartDao().deleteSpecificCartItem(productId);


    }

    public LiveData<List<CartItemRoom>> getCartItemFromInternal() {
        return database.cartDao().getAll();
    }

    public void deleteAllFromCartCount0() {
        database.cartDao().deleteAllCount0();
    }





    //############################### Login ############################//
    public void startLogin(String email, String password) {
        networkDataHelper.startLogin(email, password);

    }

    public MutableLiveData<Boolean> getIsLogged(){
        return networkDataHelper.getIsLogged();
    }

    public void startLogout() {
        networkDataHelper.startLogOut();
    }

    //############################### New Account ############################//
    public void createNewAccount(String firstName, String secondName,
                                 String phone, String email, String password) {

        networkDataHelper.startCreateNewAccount(firstName,secondName,phone,email,password);
    }

    public void startGetUserDetails(String uid) {
        networkDataHelper.startGetUserDetails(uid);
    }

    public MutableLiveData<UserItem> getCurrentUserDetails(){
        return networkDataHelper.getCurrentUserDetails();
    }



}
