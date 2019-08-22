package online.lahloba.www.lahloba.data;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.BannerItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.MarketPlace;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.data.services.LahlobaMainService;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.LocalUtils;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;

import static online.lahloba.www.lahloba.utils.Constants.CHANGE_ORDER_STATUS;
import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;
import static online.lahloba.www.lahloba.utils.Constants.GET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.GET_MAIN_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.MARKETPLACE_ID;
import static online.lahloba.www.lahloba.utils.Constants.ORDER_ID;
import static online.lahloba.www.lahloba.utils.Constants.ORDER_STATUS;
import static online.lahloba.www.lahloba.utils.Constants.RESET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_FIRSTNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PHONE;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_SECONDNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_ADDRESS_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.START_DELETE_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_EDIT_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_ADDRESSES;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_ORDERS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_USER_DETAILS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_DEFAULT_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_GOVERNORATES;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_MARKETPLACE;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_NEW_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_REMOVE_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_REORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_SET_DEFAULT_ADDRESS;

public class NetworkDataHelper {
    private static final Object LOCK = new Object();
    private static NetworkDataHelper sInstance;
    Context mContext;

    MutableLiveData<List<MainMenuItem>> mainMenuItems;
    MutableLiveData<List<SubMenuItem>> subMenuItems;
    MutableLiveData<List<CartItem>> cartItems;
    MutableLiveData<List<AddressItem>> addressItems;
    MutableLiveData<Boolean> isLogged;
    MutableLiveData<Boolean> isUserCreated;
    MutableLiveData<Boolean> isAddressAdded;
    MutableLiveData<UserItem> userDetails;
    MutableLiveData<AddressItem> defaultAddress;
    MutableLiveData<DataSnapshot> governorates;
    MutableLiveData<List<MarketPlace>> marketPlaces;
    MutableLiveData<MarketPlace> marketPlace;
    MutableLiveData<Boolean> isOrderAdded;
    private MutableLiveData<Boolean> isAddressEdited;

    private MutableLiveData<List<OrderItem>> orderItems;


    private MutableLiveData<List<ProductItem>> productsItems;
    private MutableLiveData<List<ProductItem>> favoritesItems;
    private MutableLiveData<List<BannerItem>> bannerItems;


    public NetworkDataHelper(Context applicationContext) {
        mContext = applicationContext;
        mainMenuItems = new MutableLiveData<>();
        subMenuItems = new MutableLiveData<>();
        productsItems = new MutableLiveData<>();
        addressItems = new MutableLiveData<>();
        cartItems = new MutableLiveData<>();
        governorates = new MutableLiveData<>();
        marketPlaces = new MutableLiveData<>();
        marketPlace = new MutableLiveData<>();
        orderItems = new MutableLiveData<>();
        favoritesItems = new MutableLiveData<>();
        bannerItems = new MutableLiveData<>();

        defaultAddress = new MutableLiveData<>();

        isLogged = new MutableLiveData<>();
        userDetails = new MutableLiveData<>();
        isUserCreated = new MutableLiveData<>();
        isOrderAdded = new MutableLiveData<>();
        isAddressEdited = new MutableLiveData<>();


        isUserCreated.setValue(false);

        isAddressAdded = new MutableLiveData<>();
        isAddressAdded.setValue(false);

        if (null == FirebaseAuth.getInstance().getCurrentUser()){
            isLogged.setValue(false);
        }else {
            isLogged.setValue(true);
        }
    }

    public static NetworkDataHelper getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new NetworkDataHelper(context.getApplicationContext());
            }
        }

        return sInstance;
    }



    //############################### Main Menu Item ############################//

    public void startGetMainMenuItems() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(GET_MAIN_MENU_ITEMS);
        mContext.startService(intent);

    }

    public void startFetchMainMenuItems() {

        String language = LocalUtils.getLangauge();
        DatabaseReference database = FirebaseDatabase
                .getInstance()
                .getReference("MainMenu")
                .child(language);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MainMenuItem> mainMenuItemList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    MainMenuItem item = child.getValue(MainMenuItem.class);
                    mainMenuItemList.add(item);
                }

                mainMenuItems.setValue(mainMenuItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public MutableLiveData<List<MainMenuItem>> getMainMenuItems() {
        return mainMenuItems;
    }


    //############################### Sup Menu Item ############################//
    public void startGetSupMenuItems(String subMenuId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(GET_SUB_MENU_ITEMS);
        intent.putExtra(GET_SUB_MENU_ITEMS, subMenuId);
        mContext.startService(intent);
    }

    public void startGetSubMenusWithNoChild() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.GET_SUB_MENU_ITEMS_NO_CHILD);
        mContext.startService(intent);

    }

    public void startFetchSubMenuItems(String subMenuId) {
        String language = LocalUtils.getLangauge();
        Query database = FirebaseDatabase
                .getInstance()
                .getReference("SubMenu")
                .child(language).orderByChild("parentId").equalTo(subMenuId);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SubMenuItem> subMenuItemList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    SubMenuItem item = child.getValue(SubMenuItem.class);
                    subMenuItemList.add(item);
                }
                subMenuItems.setValue(subMenuItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startFetchSubMenuNoChildItems() {
        String language = LocalUtils.getLangauge();
        Query database = FirebaseDatabase
                .getInstance()
                .getReference("SubMenu")
                .child(language).orderByChild("hasChild").equalTo(false);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SubMenuItem> subMenuItemList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    SubMenuItem item = child.getValue(SubMenuItem.class);
                    subMenuItemList.add(item);
                }
                subMenuItems.setValue(subMenuItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<SubMenuItem>> getSubMenuItems() {
        return subMenuItems;
    }

    public void clearSupMenu() {
        List<SubMenuItem> empty = new ArrayList<>();
        subMenuItems.setValue(empty);
    }


    //############################### Products ############################//
    public void startGetProductsFromCategory(String categoryId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(GET_PRODUCTS_FOR_CATEGORY);
        intent.putExtra(GET_PRODUCTS_FOR_CATEGORY, categoryId);
        mContext.startService(intent);
    }

    public void startFetchProductsForCategory(String categoyId) {
        double lat;
        double lan;

        if (null != SharedPreferencesManager.getCurrentLocationLat(mContext)){
            lat = Double.parseDouble(SharedPreferencesManager.getCurrentLocationLat(mContext));
            lan = Double.parseDouble(SharedPreferencesManager.getCurrentLocationLan(mContext));
        }else {
            return;
        }



        List<ProductItem> products = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MarketPlaceLocation");
        GeoFire geoFire = new GeoFire(ref);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lat,lan),50);
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {

                String language = LocalUtils.getLangauge();
                Query database = FirebaseDatabase
                        .getInstance()
                        .getReference("Product")
                        .child(language).orderByChild("parentId-marketPlaceId").equalTo(categoyId+"-"+dataSnapshot.getKey());


                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            ProductItem item = child.getValue(ProductItem.class);
                            item.setCurrency("EGP");
                            products.add(item);
                        }

                        productsItems.setValue(products);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    public MutableLiveData<List<ProductItem>> getProducts() {
        return productsItems;
    }


    public void startGetProductForCategoryAndUser(String category) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.GET_PRODUCTS_FOR_CATEGORY_AND_USER);
        intent.putExtra(Constants.CATEGORY_ID, category);
        mContext.startService(intent);
    }


    public void startFetchProductsForCategoryAndUser(String categoyId) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (null == uid) return;
        if (null == categoyId) return;

        productsItems.postValue(null);

        String language = LocalUtils.getLangauge();

        Query database = FirebaseDatabase
                .getInstance()
                .getReference("Product")
                .child(language)
                .orderByChild("parentId-userId")
                .equalTo(categoyId+"-"+uid);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ProductItem> products = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    ProductItem item = child.getValue(ProductItem.class);
                    products.add(item);
                }

                productsItems.setValue(products);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void startChangeProductStatus(String productId, boolean isEnable) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.CHANGE_PRODUCT_STATUS);
        intent.putExtra(Constants.PRODUCT_ID, productId);
        intent.putExtra(Constants.IS_ENABLE, isEnable);
        mContext.startService(intent);
    }

    public void changeProductStatus(String productId, boolean isEnable){
        FirebaseDatabase.getInstance().getReference()
                .child("Product")
                .child("en")
                .child(productId)
                .child("status").setValue(isEnable);

        FirebaseDatabase.getInstance().getReference()
                .child("Product")
                .child("ar")
                .child(productId)
                .child("status").setValue(isEnable);
    }


    public void startChangeProductPrice(String productId, String price) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.CHANGE_PRODUCT_PRICE);
        intent.putExtra(Constants.PRODUCT_ID, productId);
        intent.putExtra(Constants.CHANGE_PRODUCT_PRICE, price);
        mContext.startService(intent);

    }

    public void changeProductPrice(String productId, String price){

        FirebaseDatabase.getInstance().getReference()
                .child("Product")
                .child("en")
                .child(productId)
                .child("price").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (null == mutableData.getValue()) return Transaction.abort();

                String mPrice = mutableData.getValue().toString();
                if (mPrice == null){
                    return Transaction.success(mutableData);
                }

                mutableData.setValue(price);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("Product")
                .child("ar")
                .child(productId)
                .child("price").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (null == mutableData.getValue()) return Transaction.abort();

                String mPrice = mutableData.getValue().toString();
                if (mPrice == null){
                    return Transaction.success(mutableData);
                }

                mutableData.setValue(price);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });




    }


    public void startEditProduct(ProductItem productItem, String language) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_EDIT_PRODUCT);
        intent.putExtra(Constants.START_EDIT_PRODUCT, productItem);
        intent.putExtra(Constants.LANGUAGE, language);
        mContext.startService(intent);
    }

    public void editProduct(ProductItem productItem, String language){
        FirebaseDatabase.getInstance().getReference()
                .child("Product")
                .child(language)
                .child(productItem.getId())
                .setValue(productItem);

    }

    //############################### Cart ############################//

    public void startGetCartItems(String userId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(GET_CART_ITEM);
        intent.putExtra(GET_CART_ITEM, userId);
        mContext.startService(intent);
    }

    public MutableLiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    public void startFetchCartItem(String userId) {
        if (userId==null){
            cartItems.postValue(null);
            return;
        }

        Query database = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(userId)
                .child("CartItems").orderByChild("count").startAt(1);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CartItem> cartItemList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    CartItem item = child.getValue(CartItem.class);
                    item.setCurrency("EGP");
                    cartItemList.add(item);
                }

                cartItems.setValue(cartItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addCartItemsToFireBase(List<CartItemRoom> cartItems, String userId) {
        for (int i=0; i< cartItems.size(); i++){

            CartItem cartItem = new CartItem();
            cartItem.setCurrency(cartItems.get(i).getCurrency());
            cartItem.setProductName(cartItems.get(i).getProductName());
            cartItem.setCount(cartItems.get(i).getCount());
            cartItem.setProductId(cartItems.get(i).getProductId());
            cartItem.setImage(cartItems.get(i).getImage());
            cartItem.setPrice(cartItems.get(i).getPrice());



            FirebaseDatabase.getInstance().getReference()
                    .child("Cart")
                    .child(userId)
                    .child("CartItems")
                    .child(cartItem.getProductId())
                    .setValue(cartItem);
        }
    }


    public void startResetFirebaseCart() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(RESET_CART_ITEM);
        mContext.startService(intent);
    }

    public void resetFirebaseCart(){
        if (FirebaseAuth.getInstance().getCurrentUser() == null)return;

        String userId = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("Cart")
                .child(userId).removeValue();

    }


    //############################### Login ############################//
    public void startLogin(String email, String password) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_LOGIN);
        intent.putExtra(START_LOGIN_EMAIL, email);
        intent.putExtra(START_LOGIN_PASSWORD, password);
        mContext.startService(intent);
    }

    public void startFirebaseLogin(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            isLogged.setValue(true);
                        }
                    }
                });
    }

    public MutableLiveData<Boolean> getIsLogged() {
        return isLogged;
    }

    public void startLogOut() {
        FirebaseAuth.getInstance().signOut();
        userDetails.setValue(null);
        isLogged.setValue(false);
    }


    public void startGetUserDetails(String uid) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_GET_CURRENT_USER_DETAILS);
        intent.putExtra(START_GET_CURRENT_USER_DETAILS, uid);
        mContext.startService(intent);
    }

    public void fetchCurrentUserDetails(String uid) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("User").child(uid);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserItem currentUser = dataSnapshot.getValue(UserItem.class);
                        userDetails.setValue(currentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<UserItem> getCurrentUserDetails() {
        return userDetails;
    }

    //############################### Create New Account ############################//

    public void startCreateNewAccount(String firstName, String secondName,
                                      String phone, String email, String password) {

        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_CREATE_NEW_ACCOUNT);
        intent.putExtra(START_CREATE_NEW_ACCOUNT_FIRSTNAME, firstName);
        intent.putExtra(START_CREATE_NEW_ACCOUNT_SECONDNAME, secondName);
        intent.putExtra(START_CREATE_NEW_ACCOUNT_PHONE, phone);
        intent.putExtra(START_CREATE_NEW_ACCOUNT_EMAIL, email);
        intent.putExtra(START_CREATE_NEW_ACCOUNT_PASSWORD, password);
        mContext.startService(intent);
    }

    public void startCreateFirebaseAccount(String firstname, String secondname,
                                           String phone, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String uid = task.getResult().getUser().getUid();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            UserItem userItem = new UserItem();
                            userItem.setId(uid);
                            userItem.setEmail(email);
                            userItem.setFirstName(firstname);
                            userItem.setLastName(secondname);
                            userItem.setMobile(phone);
                            userItem.setSeller(false);
                            userItem.setSellerId("");
                            userItem.setStatus(true);

                            mDatabase.child("User").child(uid).setValue(userItem)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        isLogged.setValue(true);
                                        isUserCreated.setValue(true);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public MutableLiveData<Boolean> getIsUserCreated() {
        return isUserCreated;
    }




    //############################### Addresses ############################//

    public void startAddNewAddress(String userId, AddressItem addressItem) {

        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_CREATE_NEW_ADDRESS);
        intent.putExtra(START_CREATE_NEW_ADDRESS, userId);
        intent.putExtra(START_CREATE_NEW_ADDRESS_ADDRESS_ITEM, addressItem);
        mContext.startService(intent);

    }

    public void addNewAddress(String userId, AddressItem addressItem) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String push = mDatabase.child("Address").child(userId).push().getKey();
        addressItem.setId(push);

        mDatabase.child("Address").child(userId).child(push).setValue(addressItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            if (addressItem.isDefaultAddress()){
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Address")
                                        .child(userId)
                                        .orderByChild("default")
                                        .equalTo(true)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren()){
                                                    AddressItem address = child.getValue(AddressItem.class);
                                                    if (!address.getId().equals(addressItem.getId())){
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Address")
                                                                .child(userId)
                                                                .child(address.getId())
                                                                .child("default")
                                                                .setValue(false);
                                                    }



                                                }

                                                isAddressAdded.setValue(true);

                                            }



                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                            }else{
                                isAddressAdded.setValue(true);
                            }


                        }
                    }
                });

    }

    public MutableLiveData<Boolean> getIsAddressAdded() {
        return isAddressAdded;
    }

    public void setIsAddressAddedFalse() {
        isAddressAdded.setValue(false);
    }

    public void startGetAddrresses(String userId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_GET_ADDRESSES);
        intent.putExtra(START_GET_ADDRESSES, userId);
        mContext.startService(intent);
    }

    public void getAddressesFromFirebase(String userId) {
        FirebaseDatabase.getInstance().getReference()
                .child("Address")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (null != dataSnapshot){
                            List<AddressItem> addressItemList = new ArrayList<>();
                            for (DataSnapshot child : dataSnapshot.getChildren()){
                                AddressItem addressItem = child.getValue(AddressItem.class);
                                addressItemList.add(addressItem);
                            }
                            addressItems.setValue(addressItemList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<List<AddressItem>> getAddressItems() {
        return addressItems;
    }

    public void startGetDefaultAddress(String uid) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_GET_DEFAULT_ADDRESS);
        intent.putExtra(START_GET_DEFAULT_ADDRESS, uid);
        mContext.startService(intent);
    }

    public void getDefaultAddressFromFirebase(String userId) {
        FirebaseDatabase.getInstance().getReference()
                .child("Address")
                .child(userId)
                .orderByChild("default")
                .equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (null != dataSnapshot){
                            for (DataSnapshot child : dataSnapshot.getChildren()){
                                AddressItem addressItem = child.getValue(AddressItem.class);
                                defaultAddress.setValue(addressItem);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<AddressItem> getDefaultAddress() {
        return defaultAddress;
    }

    public void startSetDefaultAddress(String id) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_SET_DEFAULT_ADDRESS);
        intent.putExtra(START_SET_DEFAULT_ADDRESS, id);
        mContext.startService(intent);
    }

    public void setDefaultAddress(String id) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("Address").child(userId).child(id)
                .child("defaultAddress")
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                                FirebaseDatabase.getInstance().getReference()
                                        .child("Address")
                                        .child(userId)
                                        .orderByChild("defaultAddress")
                                        .equalTo(true)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren()){
                                                    AddressItem address = child.getValue(AddressItem.class);
                                                    if (!address.getId().equals(id)){
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Address")
                                                                .child(userId)
                                                                .child(address.getId())
                                                                .child("defaultAddress")
                                                                .setValue(false);
                                                    }



                                                }


                                            }



                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });





                        }
                    }
                });

    }

    public void startDeleteAddress(String id) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_DELETE_ADDRESS);
        intent.putExtra(START_DELETE_ADDRESS, id);
        mContext.startService(intent);
    }

    public void deleteAddress(String id) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("Address").child(userId).child(id)
                .removeValue();
    }


    public void startEditAddress(AddressItem editedAddress) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_EDIT_ADDRESS);
        intent.putExtra(START_EDIT_ADDRESS, editedAddress);
        mContext.startService(intent);
    }

    public void editAddress(AddressItem addressItem){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("Address").child(userId).child(addressItem.getId())
                .setValue(addressItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isAddressEdited.setValue(true);
                        isAddressEdited.setValue(false);
                    }
                });

    }


    public MutableLiveData<Boolean> getIsAddressEdited() {
        return isAddressEdited;
    }

    //############################### Governorates ############################//
    public void startGetGovernorates() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_GET_GOVERNORATES);
        mContext.startService(intent);
    }

    public void getGovernoratesFromFirebase() {
        String language = LocalUtils.getLangauge();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Governorate").child(language).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    governorates.setValue(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<DataSnapshot> getGovernorates() {
        return governorates;
    }


    //############################### MarketPlace ############################//
    public void startGetMarketPlaceForId(String id) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_GET_MARKETPLACE);
        intent.putExtra(START_GET_MARKETPLACE,id);
        mContext.startService(intent);
    }

    public void getMarketPlaceForId(String id) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MarketPlace").child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    marketPlace.setValue(dataSnapshot.getValue(MarketPlace.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public MutableLiveData<MarketPlace> getMarketPlace() {
        return marketPlace;
    }

    public void cleerMarketPlaceForId() {
        marketPlace.setValue(null);
    }

    //############################### Orders ############################//
    public void startNewOrder(OrderItem orderItem) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_NEW_ORDER);
        intent.putExtra(START_NEW_ORDER,orderItem);
        mContext.startService(intent);
    }

    public void startAddNewOrderToFirebase(OrderItem orderItem, String from) {
        if (from.equals(START_REORDER)){
            orderItem.setOrderStatus(1);
        }

        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String pushKey = mDatabase.child("Orders").child(userId).push().getKey();
        orderItem.setId(pushKey);
        int uNumber = pushKey.hashCode();

        long orderNumber = Math.abs(uNumber);
        orderItem.setOrderNumber(orderNumber);

        Date now = new Date();
        orderItem.setDate(now);

        mDatabase.child("Orders").child(pushKey).setValue(orderItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            isOrderAdded.setValue(true);
                        }
                    }
                });
    }

    public void resetIsOrderAdded(boolean isAdded) {
        this.isOrderAdded.setValue(false);
    }

    public MutableLiveData<Boolean> getIsOrderAdded() {
        return isOrderAdded;
    }

    public void startGetCurrentOrders() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(START_GET_CURRENT_ORDERS);
        mContext.startService(intent);
    }

    public void startGetCurrentOrdersFromFirebase() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId==null) {
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Orders").orderByChild("userId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<OrderItem> orderItemsList = new ArrayList<>();
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            orderItemsList.add(item.getValue(OrderItem.class));
                        }

                        orderItems.setValue(orderItemsList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public MutableLiveData<List<OrderItem>> getCurrentOrders(){
        return orderItems;
    }

    public void startRemoveOrder(String orderId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_REMOVE_ORDER);
        intent.putExtra(START_REMOVE_ORDER,orderId);
        mContext.startService(intent);
    }

    public void removeOrderFromFireBase(String orderId) {
        if (orderId==null) {
            return;
        }

        String userId = FirebaseAuth.getInstance().getUid();
        if (userId==null) {
            return;
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Orders").child(userId).child(orderId).removeValue();
    }

    public void startReorder(OrderItem orderItem) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_REORDER);
        intent.putExtra(START_REORDER,orderItem);
        mContext.startService(intent);
    }

    public void startChangeOrderStatus(String orderId, int orderStatus) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(CHANGE_ORDER_STATUS);
        intent.putExtra(ORDER_ID,orderId);
        intent.putExtra(ORDER_STATUS,orderStatus);
        mContext.startService(intent);
    }

    public void changeOrderStatusFirebase(String orderId, int orderStatus) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)return;
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Orders").child(userId).child(orderId)
                .child("orderStatus").setValue(orderStatus);
    }


    //############################### Favorites ############################//

    public void startGetFavoriteItems() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_FAVORITES);
        mContext.startService(intent);
    }

    public void getFavoritesFromFirebase() {
        if (FirebaseAuth.getInstance().getUid()==null) {
            return;
        }

        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Favorites").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<ProductItem> productItemsList = new ArrayList<>();
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            productItemsList.add(item.getValue(ProductItem.class));
                        }

                        favoritesItems.setValue(productItemsList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




    }


    public MutableLiveData<List<ProductItem>> getFavoritesItems() {
        return favoritesItems;
    }


    //############################### Banner ############################//

    public void startGetBanner() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_BANNER);
        mContext.startService(intent);

    }

    public void getBannerFromFirebase() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BannerItem> bannerItemsList = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    bannerItemsList.add(item.getValue(BannerItem.class));
                }

                bannerItems.setValue(bannerItemsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public MutableLiveData<List<BannerItem>> getBannerItems() {
        return bannerItems;
    }




    //############################### Banner ############################//

    public void startGetSellerOrders(String marketId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.SELLER_GET_ORDERS);
        intent.putExtra(MARKETPLACE_ID, marketId);
        mContext.startService(intent);

    }

    public void getSellerOrders(String marketId) {
        String uid = FirebaseAuth.getInstance().getUid();

        if (uid == null)return;
        orderItems.postValue(null);



        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(uid)
                .child("seller")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                        if (!dataSnapshot.exists()) return;

                        boolean isSeller = Boolean.parseBoolean(dataSnapshot.getValue().toString());





                        if (!isSeller) return;



                        /**
                         * for every market place get orders
                         */

                        mDatabase.child("Orders")
                                .orderByChild("marketplaceId")
                                .equalTo(marketId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot ordersData) {
                                        if (!ordersData.exists()) return;
                                        List<OrderItem> orderItemList = new ArrayList<>();
                                        for (DataSnapshot orderData: ordersData.getChildren()){
                                            orderItemList.add(orderData.getValue(OrderItem.class));
                                        }

                                        orderItems.setValue(orderItemList);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void startGetMarketPlacesBySeller(String uid) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_SELLER_MARKETPLACE);
        intent.putExtra(EXTRA_USER_ID, uid);
        mContext.startService(intent);
    }

    public void getMarketPlacesForSeller(String uid) {
        marketPlaces.postValue(null);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("MarketPlace").orderByChild("sellerId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) return;
                        List<MarketPlace> marketPlaceList = new ArrayList<>();
                        for (DataSnapshot marketData : dataSnapshot.getChildren()){
                            marketPlaceList.add(marketData.getValue(MarketPlace.class));
                        }

                        marketPlaces.setValue(marketPlaceList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<List<MarketPlace>> getMarketPlaces() {
        return marketPlaces;
    }



}
