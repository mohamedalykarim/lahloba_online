package online.lahloba.www.lahloba.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import online.lahloba.www.lahloba.data.model.AddressItem;
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

import static online.lahloba.www.lahloba.utils.Constants.GET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.GET_MAIN_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.RESET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_FIRSTNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PHONE;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_SECONDNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_ADDRESS_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.START_DELETE_ADDRESS;
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
    MutableLiveData<MarketPlace> marketPlace;
    MutableLiveData<Boolean> isOrderAdded;
    private MutableLiveData<List<OrderItem>> orderItems;


    private MutableLiveData<List<ProductItem>> productsItems;


    public NetworkDataHelper(Context applicationContext) {
        mContext = applicationContext;
        mainMenuItems = new MutableLiveData<>();
        subMenuItems = new MutableLiveData<>();
        productsItems = new MutableLiveData<>();
        addressItems = new MutableLiveData<>();
        cartItems = new MutableLiveData<>();
        governorates = new MutableLiveData<>();
        marketPlace = new MutableLiveData<>();
        orderItems = new MutableLiveData<>();

        defaultAddress = new MutableLiveData<>();

        isLogged = new MutableLiveData<>();
        userDetails = new MutableLiveData<>();
        isUserCreated = new MutableLiveData<>();
        isOrderAdded = new MutableLiveData<>();


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

    public MutableLiveData<List<ProductItem>> getProductsOfCategory() {
        return productsItems;
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

    //############################### Create New Account ############################//





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

                            if (addressItem.isDefault()){
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
                .child("default")
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

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
                                                    if (!address.getId().equals(id)){
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Address")
                                                                .child(userId)
                                                                .child(address.getId())
                                                                .child("default")
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

    public void startAddNewOrderToFirebase(OrderItem orderItem) {
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String pushKey = mDatabase.child("Orders").child(userId).push().getKey();
        orderItem.setId(pushKey);
        int uNumber = pushKey.hashCode();

        long orderNumber = Math.abs(uNumber);
        orderItem.setOrderNumber(orderNumber);

        mDatabase.child("Orders").child(userId).child(pushKey).setValue(orderItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mDatabase.child("Cart").child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    isOrderAdded.setValue(true);
                                }
                            });
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

        mDatabase.child("Orders").child(userId)
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


}
