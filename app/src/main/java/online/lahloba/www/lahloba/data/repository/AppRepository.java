package online.lahloba.www.lahloba.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.BannerItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.GovernorateItem;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
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


    //############################### Proucts ############################//
    public void startGetSProductItems(String categoryId) {
        networkDataHelper.startGetProductsFromCategory(categoryId);
    }

    public MutableLiveData<List<ProductItem>> getProductsForCategory() {
        return networkDataHelper.getProducts();
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

    public void deleteAllFromInternalCart() {
        database.cartDao().deleteAll();
    }


    public void startResetFirebaseCart() {
        networkDataHelper.startResetFirebaseCart();
    }

    public void startDeleteAllFromCart() {
        networkDataHelper.startDeleteAllFromCart();
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

    public MutableLiveData<Boolean> getIsUserCreated() {
        return networkDataHelper.getIsUserCreated();
    }


    public void addCartItemsToFireBase(String userId) {
        database.cartDao().getAllWithCount().observeForever(cartItems->{
            if (cartItems!= null){
                networkDataHelper.addCartItemsToFireBase(cartItems, userId);
                Injector.getExecuter().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.cartDao().deleteAll();
                    }
                });
            }
        });




    }

    //############################### Address ############################//

    public void startAddNewAddress(String userId, AddressItem addressItem) {

        networkDataHelper.startAddNewAddress(userId, addressItem);
    }

    public MutableLiveData<Boolean> getIsAddressAdded(){
        return networkDataHelper.getIsAddressAdded();
    }

    public void setIsAddressAddedFalse() {
        networkDataHelper.setIsAddressAddedFalse();
    }

    public void startGetAddrresses(String userId) {
        networkDataHelper.startGetAddrresses(userId);
    }

    public MutableLiveData<List<AddressItem>> getAddressItems() {
        return networkDataHelper.getAddressItems();
    }

    public void startGetDefaultAddress(String uid) {
        networkDataHelper.startGetDefaultAddress(uid);
    }

    public MutableLiveData<AddressItem> getDefaultAddress() {
        return networkDataHelper.getDefaultAddress();
    }

    public void startSetDefaultAddress(String id) {
        networkDataHelper.startSetDefaultAddress(id);
    }

    public void startDeleteAddress(String id) {
        networkDataHelper.startDeleteAddress(id);
    }

    //############################### GoverCities ############################//

    public void startGetGovernorate() {
        networkDataHelper.startGetGovernorate();
    }

    public MutableLiveData<List<GovernorateItem>> getGovernorates() {
        return networkDataHelper.getGovernorates();
    }

    
    public void startGetCities() {
        networkDataHelper.startGetCities();
    }

    public MutableLiveData<List<CityItem>> getCities() {
        return networkDataHelper.getCities();
    }

    //############################### Market Place ############################//


    public void startGetMarketPlaceForId(String id) {
        networkDataHelper.startGetMarketPlaceForId(id);
    }

    public MutableLiveData<MarketPlace> getMarketPlace() {
        return networkDataHelper.getMarketPlace();
    }

    public void cleerMarketPlaceForId() {
        networkDataHelper.cleerMarketPlaceForId();
    }

    public void insertMarketPlaceToInternal(MarketPlace marketPlace){
        Injector.getExecuter().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long m = database.marketPlaceDao().insert(marketPlace);

            }
        });
    }

    public LiveData<List<MarketPlace>> getMarketPlacesFromInternal(List<String> ids) {
        return database.marketPlaceDao().getSpecificMarketPlaces(ids);
    }

    public LiveData<MarketPlace> getMarketPlaceFromInternal(String id) {

        return database.marketPlaceDao().getSpecificMarketPlace(id);
    }


    //############################### Orders ############################//

    public void startNewOrder(OrderItem orderItem) {
        networkDataHelper.startNewOrder(orderItem);
    }

    public void resetIsOrderAdded(boolean isAdded) {
        networkDataHelper.resetIsOrderAdded(isAdded);
    }

    public MutableLiveData<Boolean> getIsOrderAdded() {
        return networkDataHelper.getIsOrderAdded();
    }

    public void startGetCurrentOrders() {
        networkDataHelper.startGetCurrentOrders();
    }

    public MutableLiveData<List<OrderItem>> getCurrentOrders(){
        return networkDataHelper.getCurrentOrders();
    }

    public void startRemoveOrder(String orderId) {
        networkDataHelper.startRemoveOrder(orderId);
    }

    public void startEditAddress(AddressItem editedAddress) {
        networkDataHelper.startEditAddress(editedAddress);
    }

    public MutableLiveData<Boolean> getIsAddressEdited() {
        return networkDataHelper.getIsAddressEdited();
    }

    public void startChangeOrderStatus(String orderId, int orderStatus) {
        networkDataHelper.startChangeOrderStatus(orderId, orderStatus);
    }

    public void startGetUserForOrder(String userId) {
        networkDataHelper.startGetUserForOrder(userId);
    }

    public MutableLiveData<UserItem> getUserForOrder() {
        return networkDataHelper.getUserForOrder();
    }


    public void startGetOrderById(String orderId) {
        networkDataHelper.startGetOrderById(orderId);
    }

    public MutableLiveData<OrderItem> getOrderItem() {
        return networkDataHelper.getOrderItem();
    }



    //############################### Favorites ############################//

    public void startGetFavoriteItems() {
        networkDataHelper.startGetFavoriteItems();
    }


    public MutableLiveData<List<ProductItem>> getFavoritesItems() {
        return networkDataHelper.getFavoritesItems();
    }


    //############################### Banner ############################//


    public void startGetBanner() {
        networkDataHelper.startGetBanner();
    }

    public MutableLiveData<List<BannerItem>> getBannerItems() {
        return networkDataHelper.getBannerItems();
    }

    public void startReorder(OrderItem orderItem) {
        networkDataHelper.startReorder(orderItem);
    }


}
