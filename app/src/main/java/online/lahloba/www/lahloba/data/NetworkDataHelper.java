package online.lahloba.www.lahloba.data;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import online.lahloba.www.lahloba.data.model.ProductOption;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.data.services.LahlobaMainService;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.LocalUtils;
import online.lahloba.www.lahloba.utils.StatusUtils;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;


public class NetworkDataHelper {
    private static final Object LOCK = new Object();
    private static NetworkDataHelper sInstance;
    private final DatabaseReference firebaseRef;
    private Context mContext;
    

    private Bitmap bitmap;

    private MutableLiveData<List<MainMenuItem>> mainMenuItems;
    private MutableLiveData<List<SubMenuItem>> subMenuItems;
    private MutableLiveData<List<CartItem>> cartItems;
    private MutableLiveData<CartItem> cartItem;
    private MutableLiveData<List<AddressItem>> addressItems;
    private MutableLiveData<Boolean> isLogged;
    private MutableLiveData<Boolean> isUserCreated;
    private MutableLiveData<Boolean> isAddressAdded;
    private MutableLiveData<Boolean> oldFarProductExistsInCart;

    private MutableLiveData<UserItem> userDetails;
    private MutableLiveData<AddressItem> defaultAddress;
    private MutableLiveData<List<CityItem>> cities;
    private MutableLiveData<List<GovernorateItem>> governorates;
    private MutableLiveData<List<MarketPlace>> marketPlaces;
    private MutableLiveData<MarketPlace> marketPlace;
    private MutableLiveData<Boolean> isOrderAdded;
    private MutableLiveData<Boolean> isAddressEdited;

    private MutableLiveData<List<OrderItem>> orderItems;


    private MutableLiveData<List<ProductItem>> productsItems;
    private MutableLiveData<ProductItem> productItem;
    private MutableLiveData<DataSnapshot> productOptions;


    private MutableLiveData<List<FavoriteItem>> favoritesItems;
    private MutableLiveData<FavoriteItem> favoritesItem;
    private MutableLiveData<List<BannerItem>> bannerItems;

    private MutableLiveData<ProductItem> enProductItemForEdit;
    private MutableLiveData<ProductItem> arProductItemForEdit;

    private MutableLiveData<UserItem> userForOrder;
    private MutableLiveData<OrderItem> orderItem;
    private MutableLiveData<List<DeliveryArea>> deliveryAreas;
    private MutableLiveData<List<String>> deliveriesId;


    private NetworkDataHelper(Context applicationContext) {
        mContext = applicationContext;
        mainMenuItems = new MutableLiveData<>();
        subMenuItems = new MutableLiveData<>();
        addressItems = new MutableLiveData<>();
        cartItems = new MutableLiveData<>();
        cartItem = new MutableLiveData<>();
        cities = new MutableLiveData<>();
        deliveryAreas = new MutableLiveData<>();
        governorates = new MutableLiveData<>();
        marketPlaces = new MutableLiveData<>();
        marketPlace = new MutableLiveData<>();
        orderItems = new MutableLiveData<>();
        favoritesItems = new MutableLiveData<>();
        favoritesItem = new MutableLiveData<>();
        bannerItems = new MutableLiveData<>();

        defaultAddress = new MutableLiveData<>();

        isLogged = new MutableLiveData<>();
        userDetails = new MutableLiveData<>();
        isUserCreated = new MutableLiveData<>();
        isOrderAdded = new MutableLiveData<>();
        isAddressEdited = new MutableLiveData<>();
        oldFarProductExistsInCart = new MutableLiveData<>();


        productsItems = new MutableLiveData<>();
        productItem = new MutableLiveData<>();
        enProductItemForEdit = new MutableLiveData<>();
        arProductItemForEdit = new MutableLiveData<>();
        productOptions = new MutableLiveData<>();

        userForOrder = new MutableLiveData<>();
        orderItem = new MutableLiveData<>();

        deliveriesId = new MutableLiveData<>();


        isUserCreated.setValue(false);

        isAddressAdded = new MutableLiveData<>();
        isAddressAdded.setValue(false);

        if (null == FirebaseAuth.getInstance().getCurrentUser()){
            isLogged.setValue(false);
        }else {
            isLogged.setValue(true);
        }
        
        firebaseRef = FirebaseDatabase.getInstance().getReference();
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
        intent.setAction(Constants.GET_MAIN_MENU_ITEMS);
        mContext.startService(intent);

    }

    public void startFetchMainMenuItems() {

        String language = LocalUtils.getLangauge();
        firebaseRef.child("MainMenu")
                .child(language).addValueEventListener(new ValueEventListener() {
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
        intent.setAction(Constants.GET_SUB_MENU_ITEMS);
        intent.putExtra(Constants.GET_SUB_MENU_ITEMS, subMenuId);
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
        intent.setAction(Constants.GET_PRODUCTS_FOR_CATEGORY);
        intent.putExtra(Constants.GET_PRODUCTS_FOR_CATEGORY, categoryId);
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
                        .child(language).orderByChild("parentIdMarketPlaceId").equalTo(categoyId+"-"+dataSnapshot.getKey());


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
                .orderByChild("parentIdSellerId")
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
        firebaseRef.child("Product")
                .child("en")
                .child(productId)
                .child("status").setValue(isEnable);

        firebaseRef.child("Product")
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

        firebaseRef.child("Product")
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

        firebaseRef.child("Product")
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
        firebaseRef.child("Product")
                .child(language)
                .child(productItem.getId())
                .setValue(productItem);
    }



    public void startAddNewProduct(Bitmap myBitmap, ProductItem enProductItem, ProductItem arProductItem) {
        bitmap = myBitmap;



        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_ADD_NEW_PRODUCT);
        intent.putExtra(Constants.START_ADD_NEW_PRODUCT_EN_PRODUCT, enProductItem);
        intent.putExtra(Constants.START_ADD_NEW_PRODUCT_AR_PRODUCT, arProductItem);
        mContext.startService(intent);
    }

    public void addNewProduct(ProductItem enProduct, ProductItem arProduct){

        if (bitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference fileRef = storageRef.child("images/product/"+FirebaseAuth.getInstance().getUid()+"/thumb").child("product"+enProduct.getId()+".jpg");

            UploadTask uploadTask = fileRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){

                        enProduct.setImage("images/product/"+FirebaseAuth.getInstance().getUid()+"/thumb/product"+enProduct.getId()+".jpg");
                        arProduct.setImage("images/product/"+FirebaseAuth.getInstance().getUid()+"/thumb/product"+enProduct.getId()+".jpg");


                        firebaseRef
                                .child("Product")
                                .child("en")
                                .child(enProduct.getId())
                                .setValue(enProduct);


                        firebaseRef
                                .child("Product")
                                .child("ar")
                                .child(enProduct.getId())
                                .setValue(arProduct);


                        bitmap = null;
                    }
                }
            });





        }else{

            enProduct.setImage("images/product/"+FirebaseAuth.getInstance().getUid()+"/thumb/product"+enProduct.getId()+".jpg");
            arProduct.setImage("images/product/"+FirebaseAuth.getInstance().getUid()+"/thumb/product"+enProduct.getId()+".jpg");


            firebaseRef
                    .child("Product")
                    .child("en")
                    .child(enProduct.getId())
                    .setValue(enProduct);


            firebaseRef
                    .child("Product")
                    .child("ar")
                    .child(enProduct.getId())
                    .setValue(arProduct);
        }










    }


    public void startGetProductById(String productId, String language) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_PRODUCT);
        intent.putExtra(Constants.START_GET_PRODUCT, productId);
        intent.putExtra(Constants.LANGUAGE, language);
        mContext.startService(intent);
    }

    public void getProduct(String productId, String language){
        firebaseRef
                .child("Product")
                .child(language)
                .child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            productItem.setValue(dataSnapshot.getValue(ProductItem.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<ProductItem> getProductItem() {
        return productItem;
    }


    public void startGetProductForEdit(String productId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_PRODUCT_FOR_EDIT);
        intent.putExtra(Constants.START_GET_PRODUCT_FOR_EDIT, productId);
        mContext.startService(intent);
    }

    public void getProductForEdit(String productId){

        firebaseRef
                .child("Product")
                .child("en")
                .child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            enProductItemForEdit.setValue(dataSnapshot.getValue(ProductItem.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        firebaseRef
                .child("Product")
                .child("ar")
                .child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            arProductItemForEdit.setValue(dataSnapshot.getValue(ProductItem.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public MutableLiveData<ProductItem> getEnProductItemForEdit() {
        return enProductItemForEdit;
    }

    public MutableLiveData<ProductItem> getArProductItemForEdit() {
        return arProductItemForEdit;
    }


    public void startGetProductOptions(String productId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_PRODUCT_OPTIONS);
        intent.putExtra(Constants.START_GET_PRODUCT_OPTIONS, productId);
        mContext.startService(intent);

    }

    public void getProductOptionsFromFirebase(String productId) {
        firebaseRef.child("ProductOptions").child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())return;

                        productOptions.setValue(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    };

    public MutableLiveData<DataSnapshot> getProductOptions() {
        return productOptions;
    }

    public void startAddOptionToCartItem(String productId, ProductOption productOption) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_ADD_OPTION_TO_CART_ITEM);
        intent.putExtra(Constants.START_ADD_OPTION_TO_CART_ITEM, productOption);
        intent.putExtra(Constants.PRODUCT_ID, productId);
        mContext.startService(intent);
    }

    public void addOptionToCartItem(String productId, ProductOption productOption){
        if (productOption == null)return;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;

        Log.v("sss", productId + " productID " + productOption.getOptionId() + " " + productOption.getOptionKey()+ " "+productOption.getOptionValue());


        firebaseRef.child("Cart").child(uid).child("CartItems").child(productId)
                .child("Options").child(productOption.getOptionId()).setValue(productOption);
    }

    //############################### Cart ############################//

    public void startGetCartItems(String userId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.GET_CART_ITEM);
        intent.putExtra(Constants.GET_CART_ITEM, userId);
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

    public void addCartItemsToFireBase(List<CartItem> cartItems, String userId) {
        for (int i=0; i< cartItems.size(); i++){
            firebaseRef
                    .child("Cart")
                    .child(userId)
                    .child("CartItems")
                    .child(cartItems.get(i).getProductId())
                    .setValue(cartItem);
        }
    }


    public void startResetFirebaseCart() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.RESET_CART_ITEM);
        mContext.startService(intent);
    }

    public void resetFirebaseCart(){
        if (FirebaseAuth.getInstance().getCurrentUser() == null)return;

        String userId = FirebaseAuth.getInstance().getUid();

        firebaseRef
                .child("Cart")
                .child(userId).removeValue();

    }


    public void startDeleteAllFromCart() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_DELETE_CART);
        mContext.startService(intent);

    }

    public void deleteAllFromCart(){
        String uid = FirebaseAuth.getInstance().getUid();

        firebaseRef
                .child("Cart")
                .child(uid)
                .removeValue();
    }

    public void startAddProductToFirebaseCart(ProductItem productItem, HashMap<String, ProductOption> productOptions) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_ADD_PRODUCT_TO_FIREBASE_CART);
        intent.putExtra(Constants.START_ADD_PRODUCT_TO_FIREBASE_CART, productItem);
        intent.putExtra(Constants.PRODUCT_OPTIONS, productOptions);
        mContext.startService(intent);

    }

    public void addProductToFirebaseCart(ProductItem productItem, HashMap<String, ProductOption> productOptionHashMap){
        if (productItem == null )return;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        CartItem cartItem = new CartItem();
        cartItem.setId(productItem.getId());
        cartItem.setCount(1);
        cartItem.setProductId(productItem.getId());
        cartItem.setImage(productItem.getImage());
        cartItem.setPrice(productItem.getPrice());
        cartItem.setProductName(productItem.getTitle());
        cartItem.setMarketId(productItem.getMarketPlaceId());
        cartItem.setPoint(productItem.getPoint());

        if (productOptionHashMap != null){
            cartItem.setOptions(productOptionHashMap);
        }


        firebaseRef.child("Cart").child(uid).child("CartLocation")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            /**
                             * Cart Location is set
                             */
                            Location cartLocation = new Location("");
                            cartLocation.setLatitude(Double.parseDouble(dataSnapshot.child("lat").getValue().toString()));
                            cartLocation.setLongitude(Double.parseDouble(dataSnapshot.child("lon").getValue().toString()));

                            Location myLocation = new Location("");
                            myLocation.setLatitude(Double.parseDouble(SharedPreferencesManager.getCurrentLocationLat(mContext)));
                            myLocation.setLongitude(Double.parseDouble(SharedPreferencesManager.getCurrentLocationLan(mContext)));

                            if (cartLocation.distanceTo(myLocation)/1000 < 30){
                                firebaseRef.child("Cart").child(uid).child("CartItems").child(cartItem.getId())
                                        .setValue(cartItem);

                            }else{
                                /**
                                 * Old far Poducts exists
                                 */

                                oldFarProductExistsInCart.setValue(true);
                                oldFarProductExistsInCart.setValue(false);


                            }


                        }else {
                            /**
                             * Cart Location not set
                             */
                            firebaseRef.child("Cart").child(uid).child("CartItems").child(cartItem.getProductId())
                                    .setValue(cartItem);

                            HashMap<String,Object> newCartLocation = new HashMap<>();
                            newCartLocation.put("lat", SharedPreferencesManager.getCurrentLocationLat(mContext));
                            newCartLocation.put("lon", SharedPreferencesManager.getCurrentLocationLan(mContext));

                            firebaseRef.child("Cart").child(uid).child("CartLocation").setValue(newCartLocation);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public MutableLiveData<Boolean> getOldFarProductExistsInCart() {
        return oldFarProductExistsInCart;
    }

    public void startGetCartItemById(String productId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_CART_ITEM_BY_ID);
        intent.putExtra(Constants.START_GET_CART_ITEM_BY_ID, productId);
        mContext.startService(intent);

    }

    public void getCartItemById(String productId){
        if (productId == null)return;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        firebaseRef.child("Cart").child(uid).child("CartItems").child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())return;
                        try {
                            cartItem.setValue(dataSnapshot.getValue(CartItem.class));
                        }catch (ClassCastException e){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<CartItem> getCartItem() {
        return cartItem;
    }


    public void startAddToCartProductCount(String productId, HashMap<String, ProductOption> productOptions){
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_ADD_TO_CART_PRODUCT_COUNT);
        intent.putExtra(Constants.START_ADD_TO_CART_PRODUCT_COUNT, productId);
        intent.putExtra(Constants.PRODUCT_OPTIONS, productOptions);
        mContext.startService(intent);

    }

    public void addToCartProductCount(String productId, HashMap<String, ProductOption> productOptionHashMap){
        if (productId == null)return;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;


        firebaseRef.child("Cart")
                .child(uid)
                .child("CartItems")
                .child(productId).child("count")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Object count = mutableData.getValue();

                        if(null == count ){
                            return Transaction.success(mutableData);
                        }else {
                            int countInt = Integer.parseInt("0"+count);
                            mutableData.setValue(countInt + 1);

                            return Transaction.success(mutableData);
                        }

                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    }
                });

        if (productOptionHashMap != null){
            firebaseRef.child("Cart")
                    .child(uid)
                    .child("CartItems")
                    .child(productId).child("options")
                    .setValue(productOptionHashMap);
        }
    }

    public void startRemoveFromCartProductCount(String productId, HashMap<String, ProductOption> productOptions){
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_REMOVE_FROM_CART_PRODUCT_COUNT);
        intent.putExtra(Constants.START_REMOVE_FROM_CART_PRODUCT_COUNT, productId);
        intent.putExtra(Constants.PRODUCT_OPTIONS, productOptions);
        mContext.startService(intent);

    }

    public void removeFromCartProductCount(String productId, HashMap<String, ProductOption> productOptionHashMap){
        if (productItem == null)return;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;

        firebaseRef.child("Cart")
                .child(uid)
                .child("CartItems")
                .child(productId).child("count")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Object count = mutableData.getValue();

                        if(null == count ){
                            return Transaction.success(mutableData);
                        }else{
                            int countInt = Integer.parseInt("0"+count);
                            if(countInt > 0){
                                mutableData.setValue(countInt - 1);
                                return Transaction.success(mutableData);
                            }

                            return Transaction.success(mutableData);
                        }

                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    }
                });

        if (productOptionHashMap != null){
            firebaseRef.child("Cart")
                    .child(uid)
                    .child("CartItems")
                    .child(productId).child("options")
                    .setValue(productOptionHashMap);
        }

    }

    //############################### Login ############################//
    public void startLogin(String email, String password) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_LOGIN);
        intent.putExtra(Constants.START_LOGIN_EMAIL, email);
        intent.putExtra(Constants.START_LOGIN_PASSWORD, password);
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

    public void setIsLogged(boolean isLogged) {
        this.isLogged.setValue(isLogged);
    }

    public void startLogOut() {
        FirebaseAuth.getInstance().signOut();

        userDetails.setValue(null);
        isLogged.setValue(false);
    }


    public void startGetUserDetails(String uid) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_CURRENT_USER_DETAILS);
        intent.putExtra(Constants.START_GET_CURRENT_USER_DETAILS, uid);
        mContext.startService(intent);
    }

    public void fetchCurrentUserDetails(String uid) {
        if (uid == null) return;
        
        DatabaseReference mDatabase = firebaseRef
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


    public void startUpdateUserDetails(UserItem userDetails) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_UPDATE_USER_DETAILS);
        intent.putExtra(Constants.USER_ITEM, userDetails);
        mContext.startService(intent);
    }

    public void updateUserDetails(UserItem userItem){
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;
        if (!uid.equals(userItem.getId()))return;

        firebaseRef.child("User")
                .child(uid)
                .setValue(userItem);

    }


    public void startUpdateMessagingToken() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_UPDATE_MESSAGING_TOKEN);
        mContext.startService(intent);

    }

    public void updateMessagingToken() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        FirebaseDatabase.getInstance().getReference()
                                .child("User")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("notificationToken")
                                .setValue(token);

                    }
                });




    }

    public void startLoggedByPhone() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_LOGGED_BY_PHONE);
        mContext.startService(intent);
    }

    public void loggedByPhone() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;

        firebaseRef.child("User")
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            updateMessagingToken();
                        }else {
                            UserItem userItem = new UserItem();
                            userItem.setId(uid);
                            userItem.setFirstName("");
                            userItem.setLastName("");
                            userItem.setMobile("");
                            userItem.setEmail("");
                            userItem.setSeller(false);
                            userItem.setDeliverySupervisor(false);
                            userItem.setDelivery(false);
                            userItem.setStatus(true);
                            userItem.setLat(0);
                            userItem.setLan(0);
                            userItem.setPoints(0);
                            userItem.setNotificationToken("");

                            firebaseRef.child("User")
                                    .child(uid)
                                    .setValue(userItem)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                           updateMessagingToken();
                                        }
                                    });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //############################### Create New Account ############################//

    public void startCreateNewAccount(String firstName, String secondName,
                                      String phone, String email, String password) {

        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_CREATE_NEW_ACCOUNT);
        intent.putExtra(Constants.START_CREATE_NEW_ACCOUNT_FIRSTNAME, firstName);
        intent.putExtra(Constants.START_CREATE_NEW_ACCOUNT_SECONDNAME, secondName);
        intent.putExtra(Constants.START_CREATE_NEW_ACCOUNT_PHONE, phone);
        intent.putExtra(Constants.START_CREATE_NEW_ACCOUNT_EMAIL, email);
        intent.putExtra(Constants.START_CREATE_NEW_ACCOUNT_PASSWORD, password);
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
                            DatabaseReference mDatabase = firebaseRef;
                            UserItem userItem = new UserItem();
                            userItem.setId(uid);
                            userItem.setEmail(email);
                            userItem.setFirstName(firstname);
                            userItem.setLastName(secondname);
                            userItem.setMobile(phone);
                            userItem.setSeller(false);
                            userItem.setStatus(true);
                            userItem.setPoints(0);
                            userItem.setDelivery(false);
                            userItem.setDeliverySupervisor(false);

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
        intent.setAction(Constants.START_CREATE_NEW_ADDRESS);
        intent.putExtra(Constants.START_CREATE_NEW_ADDRESS, userId);
        intent.putExtra(Constants.START_CREATE_NEW_ADDRESS_ADDRESS_ITEM, addressItem);
        mContext.startService(intent);

    }

    public void addNewAddress(String userId, AddressItem addressItem) {
        DatabaseReference mDatabase = firebaseRef;
        String push = mDatabase.child("Address").child(userId).push().getKey();
        addressItem.setId(push);

        mDatabase.child("Address").child(userId).child(push).setValue(addressItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            if (addressItem.isDefaultAddress()){
                                firebaseRef
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
                                                        firebaseRef
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
        intent.setAction(Constants.START_GET_ADDRESSES);
        intent.putExtra(Constants.START_GET_ADDRESSES, userId);
        mContext.startService(intent);
    }

    public void getAddressesFromFirebase(String userId) {
        firebaseRef
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
        intent.setAction(Constants.START_GET_DEFAULT_ADDRESS);
        intent.putExtra(Constants.START_GET_DEFAULT_ADDRESS, uid);
        mContext.startService(intent);
    }

    public void getDefaultAddressFromFirebase(String userId) {
        firebaseRef
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
        intent.setAction(Constants.START_SET_DEFAULT_ADDRESS);
        intent.putExtra(Constants.START_SET_DEFAULT_ADDRESS, id);
        mContext.startService(intent);
    }

    public void setDefaultAddress(String id) {
        DatabaseReference mDatabase = firebaseRef;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("Address").child(userId).child(id)
                .child("defaultAddress")
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                                firebaseRef
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
                                                        firebaseRef
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
        intent.setAction(Constants.START_DELETE_ADDRESS);
        intent.putExtra(Constants.START_DELETE_ADDRESS, id);
        mContext.startService(intent);
    }

    public void deleteAddress(String id) {
        DatabaseReference mDatabase = firebaseRef;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("Address").child(userId).child(id)
                .removeValue();
    }


    public void startEditAddress(AddressItem editedAddress) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_EDIT_ADDRESS);
        intent.putExtra(Constants.START_EDIT_ADDRESS, editedAddress);
        mContext.startService(intent);
    }

    public void editAddress(AddressItem addressItem){
        DatabaseReference mDatabase = firebaseRef;
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

    //############################### Cities ############################//
    public void startGetGovernorate() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_GOVERNORATE);
        mContext.startService(intent);

    }

    public void getGovernorateFromFirebase() {
        String language = LocalUtils.getLangauge();

        DatabaseReference mDatabase = firebaseRef;
        mDatabase.child("Governorate").child(language).child("Egypt")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            List<GovernorateItem> governorateItems = new ArrayList<>();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                governorateItems.add(child.getValue(GovernorateItem.class));
                            }

                            governorates.setValue(governorateItems);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<List<GovernorateItem>> getGovernorates() {
        return governorates;
    }

    public void startGetCities() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_CITIES);
        mContext.startService(intent);
    }

    public void getCitiesFromFirebase() {
        String language = LocalUtils.getLangauge();

        DatabaseReference mDatabase = firebaseRef;
        mDatabase.child("City").child(language)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    List<CityItem> cityItems = new ArrayList<>();
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        cityItems.add(child.getValue(CityItem.class));
                    }

                    cities.setValue(cityItems);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public MutableLiveData<List<CityItem>> getCities() {
        return cities;
    }


    //############################### MarketPlace ############################//
    public void startGetMarketPlaceForId(String id) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_MARKETPLACE);
        intent.putExtra(Constants.START_GET_MARKETPLACE,id);
        mContext.startService(intent);
    }

    public void getMarketPlaceForId(String id) {

        DatabaseReference mDatabase = firebaseRef;
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
        intent.setAction(Constants.START_NEW_ORDER);
        intent.putExtra(Constants.START_NEW_ORDER,orderItem);
        mContext.startService(intent);
    }

    public void startAddNewOrderToFirebase(OrderItem orderItem, String from) {
        if (from.equals(Constants.START_REORDER)){
            orderItem.setOrderStatus(StatusUtils.ORDER_STATUS_PENDING);
            orderItem.setCityIdStatus(orderItem.getCityId()+"-"+ StatusUtils.ORDER_STATUS_PENDING);
        }

        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase = firebaseRef;
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
        intent.setAction(Constants.START_GET_CURRENT_ORDERS);
        mContext.startService(intent);
    }

    public void startGetCurrentOrdersFromFirebase() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId==null) {
            return;
        }

        DatabaseReference mDatabase = firebaseRef;

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
        intent.putExtra(Constants.START_REMOVE_ORDER,orderId);
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

        DatabaseReference mDatabase = firebaseRef;

        mDatabase.child("Orders").child(userId).child(orderId).removeValue();
    }

    public void startReorder(OrderItem orderItem) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_REORDER);
        intent.putExtra(Constants.START_REORDER,orderItem);
        mContext.startService(intent);
    }

    public void startChangeOrderStatus(String orderId, String cityId, int orderStatus) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.CHANGE_ORDER_STATUS);
        intent.putExtra(Constants.ORDER_ID,orderId);
        intent.putExtra(Constants.CITY_ID,cityId);
        intent.putExtra(Constants.ORDER_STATUS,orderStatus);
        mContext.startService(intent);
    }

    public void     changeOrderStatusFirebase(String orderId, String cityId, int orderStatus) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)return;

        firebaseRef.child("Orders")
                .child(orderId)
                .child("orderStatus")
                .setValue(orderStatus);

        firebaseRef.child("Orders")
                .child(orderId)
                .child("cityIdStatus")
                .setValue(cityId+"-"+orderStatus);
    }

    public void startGetUserForOrder(String userId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_USER_FOR_ORDER);
        intent.putExtra(Constants.START_GET_USER_FOR_ORDER,userId);
        mContext.startService(intent);

    }

    public void getUserForOrder(String userId){

        firebaseRef
                .child("User")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        userForOrder.setValue(dataSnapshot.getValue(UserItem.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<UserItem> getUserForOrder() {
        return userForOrder;
    }


    public void startGetOrderById(String orderId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_ORDER);
        intent.putExtra(Constants.START_GET_ORDER,orderId);
        mContext.startService(intent);
    }

    public void getOrderById(String orderId){
        firebaseRef
                .child("Orders")
                .child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        orderItem.setValue(dataSnapshot.getValue(OrderItem.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public MutableLiveData<OrderItem> getOrderItem () {
        return orderItem;
    }

    public void startUpdateOrder(OrderItem orderItem) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_UPDATE_ORDER);
        intent.putExtra(Constants.START_UPDATE_ORDER, orderItem);
        mContext.startService(intent);
    }

    public void updateOrder(OrderItem orderItem){
        firebaseRef.child("Orders")
                .child(orderItem.getId())
                .setValue(orderItem);
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
        DatabaseReference mDatabase = firebaseRef;

        mDatabase.child("Favorites").child(userId)
                .orderByChild("enabled").equalTo(true
        )
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<FavoriteItem> productItemsList = new ArrayList<>();
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            productItemsList.add(item.getValue(FavoriteItem.class));
                        }

                        favoritesItems.setValue(productItemsList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    public MutableLiveData<List<FavoriteItem>> getFavoritesItems() {
        return favoritesItems;
    }

    public void startGetFavoriteItem(String productId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_FAVORITE_ITEM);
        intent.putExtra(Constants.START_GET_FAVORITE_ITEM, productId);
        mContext.startService(intent);
    }

    public void getFavoriteFromFirebase(String productId) {
        if (productId == null)return;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;

        firebaseRef.child("Favorites").child(uid).child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            try {
                                favoritesItem.setValue(dataSnapshot.getValue(FavoriteItem.class));
                            }catch (ClassCastException e){

                            }
                        }else {
                            favoritesItem.setValue(new FavoriteItem());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<FavoriteItem> getFavoritesItem() {
        return favoritesItem;
    }


    public void startChangeFavoriteStatus(String productId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.CHANGE_FAVORITE_STATUS);
        intent.putExtra(Constants.CHANGE_FAVORITE_STATUS, productId);
        mContext.startService(intent);

    }

    public void changeFavoriteStatus(String productId){
        if (productItem == null)return;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;



        firebaseRef.child("Favorites").child(uid).child(productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            try {


                                FavoriteItem favoriteItem = dataSnapshot.getValue(FavoriteItem.class);
                                boolean newValue = favoriteItem.isEnabled() ? false : true;

                                firebaseRef.child("Favorites").child(uid).child(productId)
                                        .child("enabled")
                                        .setValue(newValue);


                            }catch (ClassCastException e){

                            }

                        }else {
                            FavoriteItem favoriteItem = new FavoriteItem();
                            favoriteItem.setProductId(productId);
                            favoriteItem.setEnabled(true);
                            firebaseRef.child("Favorites").child(uid).child(productId)
                                    .setValue(favoriteItem);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    //############################### Banner ############################//

    public void startGetBanner() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_BANNER);
        mContext.startService(intent);

    }

    public void getBannerFromFirebase() {
        DatabaseReference mDatabase = firebaseRef;

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
        intent.putExtra(Constants.MARKETPLACE_ID, marketId);
        mContext.startService(intent);

    }

    public void getSellerOrders(String marketId) {
        String uid = FirebaseAuth.getInstance().getUid();

        if (uid == null)return;
        orderItems.postValue(null);



        DatabaseReference mDatabase = firebaseRef;
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
        intent.putExtra(Constants.EXTRA_USER_ID, uid);
        mContext.startService(intent);
    }

    public void getMarketPlacesForSeller(String uid) {
        marketPlaces.postValue(null);
        DatabaseReference mDatabase = firebaseRef;
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


    //############################### Delivery Supervisor ############################//
    public void startGetDeliveryAreas(int areaType) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_DELIVERY_AREAS);
        intent.putExtra(Constants.START_GET_DELIVERY_AREAS, areaType);
        mContext.startService(intent);

    }

    public void getDeliveryAreasFromFirebase(int areaType) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;
        firebaseRef.child("DeliveryArea").orderByChild("uidType").equalTo(uid + "-"+areaType)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())return;
                        List<DeliveryArea> deliveryAreasList = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            deliveryAreasList.add(child.getValue(DeliveryArea.class));
                        }

                        deliveryAreas.setValue(deliveryAreasList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
        });


    }

    public MutableLiveData<List<DeliveryArea>> getDeliveryAreas() {
        return deliveryAreas;
    }

    public void startGetOrdersForDeliverysupervisor(String cityId) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_ORDERS_FOR_DELIVERY_SUPERVISOR);
        intent.putExtra(Constants.START_GET_ORDERS_FOR_DELIVERY_SUPERVISOR, cityId);
        mContext.startService(intent);
    }

    public void getOrdersForDeliverysupervisor(String cityId) {
        Query query = firebaseRef.child("Orders").orderByChild("cityIdStatus").equalTo(cityId+"-"+ StatusUtils.ORDER_STATUS_PREPARED);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())return;
                List<OrderItem> items = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    items.add(child.getValue(OrderItem.class));
                }
                orderItems.setValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startGetDeliveriesForCity(String cityId, int areaType) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_DELIVERIES_FOR_CITY);
        intent.putExtra(Constants.CITY_ID, cityId);
        intent.putExtra(Constants.DELIVERY_AREA_TYPE, areaType);
        mContext.startService(intent);

    }

    public void getDeliveriesForCity(String cityId, int areaType) {
        Log.v("string: ", cityId+areaType);

        firebaseRef.child("DeliveryArea")
                .orderByChild("cityType")
                .equalTo(cityId+"-"+areaType)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())return;
                        List<String> items = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            try {
                                items.add(child.getValue(DeliveryArea.class).getUserId());
                            }catch (Exception e){

                            }
                        }

                        deliveriesId.setValue(items);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<List<String>> getDeliveriesId() {
        return deliveriesId;
    }


    public void clearDeliveriesIdForCity() {
        deliveriesId.setValue(null);
    }

    public void startGetOrdersForDelivery() {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.START_GET_ORDERS_FOR_DELIVERY);
        mContext.startService(intent);
    }

    public void getOrdersForDelivery() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null)return;

        firebaseRef.child("Orders").orderByChild("deliveryAllocatedTo")
                .equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())return;

                        List<OrderItem> items = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            try {
                                items.add(child.getValue(OrderItem.class));
                            }catch (Exception c){

                            }
                        }

                        orderItems.setValue(items);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    //############################### Points ############################//
    public void startAddPointsToUser(String userId, int points) {
        Intent intent = new Intent(mContext, LahlobaMainService.class);
        intent.setAction(Constants.ADD_POINTS_TO_USER);
        intent.putExtra(Constants.EXTRA_USER_ID, userId);
        intent.putExtra(Constants.POINTS, points);
        mContext.startService(intent);

    }

    public void addPointsToUser(String userId, int points) {
        firebaseRef.child("User")
                .child(userId)
                .child("points")
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Object count = mutableData.getValue();

                        if(null == count ){
                            return Transaction.success(mutableData);
                        }else {
                            int countInt = Integer.parseInt("0"+count);
                            mutableData.setValue(countInt + points);

                            return Transaction.success(mutableData);
                        }

                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    }
                });
    }




    /*  ##################     RESET       #################   */

    public void resetEditPage() {
        arProductItemForEdit.setValue(null);
        enProductItemForEdit.setValue(null);
        productItem.setValue(null);
    }


    public void startResetProductListPage() {
        productsItems.setValue(null);
        cartItem.setValue(null);
    }

    public void startResetProductOptions() {
        productOptions.setValue(null);
    }



}
