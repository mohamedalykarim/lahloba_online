package online.lahloba.www.lahloba.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.database.LahlobaDatabase;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.BannerItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.data.model.DeliveryArea;
import online.lahloba.www.lahloba.data.model.FavoriteItem;
import online.lahloba.www.lahloba.data.model.GovernorateItem;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.LocalUtils;

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

    public void startResetProductListPage() {
        networkDataHelper.startResetProductListPage();
    }

    public void startGetProductById(String productId) {
        networkDataHelper.startGetProductById(productId, LocalUtils.getLangauge());
    }

    public MutableLiveData<ProductItem> getProductItem() {
        return networkDataHelper.getProductItem();
    }


    //############################### Cart ############################//
    public void startGetCartItems(String userId) {
        networkDataHelper.startGetCartItems(userId);
    }

    public MutableLiveData<List<CartItem>> getCartItems() {
        return networkDataHelper.getCartItems();
    }

    public LiveData<List<CartItem>> startgetCartItemsFromInternal() {
        return database.cartDao().getAll();
    }


    /**
     * Internal
     * insert cart item to internal
     * @param cartItem
     */
    public void insertCartItemToInternaldb(CartItem cartItem){
        Injector.getExecuter().diskIO().execute(()->{
            database.cartDao().insert(cartItem);
        });
    }

    /**
     * Internal
     * add To CartItem Count Internal db
     */
    public void addToCartItemCountInternaldb(String productId){
        Injector.getExecuter().diskIO().execute(()-> {
            int count  = database.cartDao().getSpecificCartItemNoObserve(productId).getCount();
            database.cartDao().changeCount(productId, count + 1);
        });

    }

    public void removeFromCartItemCountInternaldb(String productId){
        Injector.getExecuter().diskIO().execute(()-> {
            int count  = database.cartDao().getSpecificCartItemNoObserve(productId).getCount();
            if (count >0){
                database.cartDao().changeCount(productId, count - 1);
            }
        });

    }

    public LiveData<CartItem> getSpecificCartItemFromInternal(String productId) {
        return database.cartDao().getSpecificCartItem(productId);
    }

    public void deleteSpecificCartItem(String productId) {
        database.cartDao().deleteSpecificCartItem(productId);


    }

    public LiveData<List<CartItem>> getCartItemFromInternal() {
        return database.cartDao().getAll();
    }

    public void removeCartitemWith0CountFromInternal() {
        Injector.getExecuter().diskIO().execute(()->{
            database.cartDao().deleteAllCount0();
        });
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

    public void startAddProductToFirebaseCart(ProductItem productItem) {
        networkDataHelper.startAddProductToFirebaseCart(productItem);
    }

    public MutableLiveData<Boolean> getOldFarProductExistsInCart() {
        return networkDataHelper.getOldFarProductExistsInCart();
    }

    public void startGetCartItemById(String productId) {
        networkDataHelper.startGetCartItemById(productId);
    }


    public MutableLiveData<CartItem> getCartItem() {
        return networkDataHelper.getCartItem();
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

    public void startAddToCartProductCount(String productId) {
        networkDataHelper.startAddToCartProductCount(productId);
    }

    public void startRemoveFromCartProductCount(String productId) {
        networkDataHelper.startRemoveFromCartProductCount(productId);
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

    public void startGetGovernorates() {
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

    public void startChangeOrderStatus(String orderId, String cityId, int orderStatus) {
        networkDataHelper.startChangeOrderStatus(orderId, cityId, orderStatus);
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


    public void startUpdateOrder(OrderItem orderItem) {
        networkDataHelper.startUpdateOrder(orderItem);
    }



    //############################### Favorites ############################//

    public void startGetFavoriteItems() {
        networkDataHelper.startGetFavoriteItems();
    }


    public MutableLiveData<List<FavoriteItem>> getFavoritesItems() {
        return networkDataHelper.getFavoritesItems();
    }

    public void startGetFavoriteItem(String productId) {
        networkDataHelper.startGetFavoriteItem(productId);
    }

    public MutableLiveData<FavoriteItem> getFavoritesItem() {
        return networkDataHelper.getFavoritesItem();
    }

    public void startChangeFavoriteStatus(String productId) {
        networkDataHelper.startChangeFavoriteStatus(productId);
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



    //############################### Delivery Supervisor ############################//
    public void startGetDeliveryAreas(int areaType) {
        networkDataHelper.startGetDeliveryAreas(areaType);
    }

    public MutableLiveData<List<DeliveryArea>> getDeliveryAreas() {
        return networkDataHelper.getDeliveryAreas();
    }


    public void startGetOrdersForDeliverysupervisor(String cityId) {
        networkDataHelper.startGetOrdersForDeliverysupervisor(cityId);
    }

    public void startGetDeliveriesForCity(String cityId, int areaType) {
        networkDataHelper.startGetDeliveriesForCity(cityId, areaType);
    }

    public MutableLiveData<List<String>> getDeliveriesId() {
        return networkDataHelper.getDeliveriesId();
    }

    public void clearDeliveriesIdForCity() {
        networkDataHelper.clearDeliveriesIdForCity();
    }

    public void startGetOrdersForDelivery() {
        networkDataHelper.startGetOrdersForDelivery();
    }


    //############################### Points ############################//
    public void startAddPointsToUser(String userId, int points) {
        networkDataHelper.startAddPointsToUser(userId, points);
    }
}
