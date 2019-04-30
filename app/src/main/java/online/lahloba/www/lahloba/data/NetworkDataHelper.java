package online.lahloba.www.lahloba.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.model.room_entity.CartItemRoom;
import online.lahloba.www.lahloba.data.services.LahlobaMainService;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;
import online.lahloba.www.lahloba.utils.LocalUtils;

import static online.lahloba.www.lahloba.utils.Constants.GET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.GET_MAIN_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_FIRSTNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PHONE;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_SECONDNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_USER_DETAILS;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_PASSWORD;

public class NetworkDataHelper {
    private static final Object LOCK = new Object();
    private static NetworkDataHelper sInstance;
    Context mContext;

    MutableLiveData<List<MainMenuItem>> mainMenuItems;
    MutableLiveData<List<SubMenuItem>> subMenuItems;
    MutableLiveData<List<CartItem>> cartItems;
    MutableLiveData<Boolean> isLogged;
    MutableLiveData<Boolean> isUserCreated;
    MutableLiveData<UserItem> userDetails;
    private MutableLiveData<List<ProductItem>> productsItems;


    public NetworkDataHelper(Context applicationContext) {
        mContext = applicationContext;
        mainMenuItems = new MutableLiveData<>();
        subMenuItems = new MutableLiveData<>();
        productsItems = new MutableLiveData<>();
        cartItems = new MutableLiveData<>();
        isLogged = new MutableLiveData<>();
        userDetails = new MutableLiveData<>();
        isUserCreated = new MutableLiveData<>();
        isUserCreated.setValue(false);

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

        String language = LocalUtils.getLangauge();
        Query database = FirebaseDatabase
                .getInstance()
                .getReference("Product")
                .child(language).orderByChild("parentId").equalTo(categoyId);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ProductItem> productItemsList = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    ProductItem item = child.getValue(ProductItem.class);
                    item.setCurrency("EGP");
                    productItemsList.add(item);
                }


                productsItems.setValue(productItemsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        Query database = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(userId).orderByChild("count").startAt(1);

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
                    .child(cartItem.getProductId())
                    .setValue(cartItem);
        }
    }
}
