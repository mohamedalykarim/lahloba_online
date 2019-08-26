package online.lahloba.www.lahloba.data.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.CATEGORY_ID;
import static online.lahloba.www.lahloba.utils.Constants.CHANGE_ORDER_STATUS;
import static online.lahloba.www.lahloba.utils.Constants.CHANGE_PRODUCT_PRICE;
import static online.lahloba.www.lahloba.utils.Constants.CHANGE_PRODUCT_STATUS;
import static online.lahloba.www.lahloba.utils.Constants.EXTRA_USER_ID;
import static online.lahloba.www.lahloba.utils.Constants.GET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY;
import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY_AND_USER;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS_NO_CHILD;
import static online.lahloba.www.lahloba.utils.Constants.IS_ENABLE;
import static online.lahloba.www.lahloba.utils.Constants.LANGUAGE;
import static online.lahloba.www.lahloba.utils.Constants.MARKETPLACE_ID;
import static online.lahloba.www.lahloba.utils.Constants.ORDER_ID;
import static online.lahloba.www.lahloba.utils.Constants.ORDER_STATUS;
import static online.lahloba.www.lahloba.utils.Constants.PRODUCT_ID;
import static online.lahloba.www.lahloba.utils.Constants.RESET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.SELLER_GET_ORDERS;
import static online.lahloba.www.lahloba.utils.Constants.START_ADD_NEW_PRODUCT;
import static online.lahloba.www.lahloba.utils.Constants.START_ADD_NEW_PRODUCT_AR_PRODUCT;
import static online.lahloba.www.lahloba.utils.Constants.START_ADD_NEW_PRODUCT_BITMAP;
import static online.lahloba.www.lahloba.utils.Constants.START_ADD_NEW_PRODUCT_EN_PRODUCT;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_FIRSTNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PHONE;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_SECONDNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_ADDRESS_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.START_DELETE_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_DELETE_CART;
import static online.lahloba.www.lahloba.utils.Constants.START_EDIT_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_EDIT_PRODUCT;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_ADDRESSES;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_BANNER;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_ORDERS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_USER_DETAILS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_DEFAULT_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_FAVORITES;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_GOVERNORATES;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_MARKETPLACE;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_PRODUCT;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_PRODUCT_FOR_EDIT;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_SELLER_MARKETPLACE;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_USER_FOR_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_NEW_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_REMOVE_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_REORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_SET_DEFAULT_ADDRESS;

public class LahlobaMainService extends IntentService {
    private NetworkDataHelper networkDataHelper;

    public LahlobaMainService() {
        super("LahlobaMainService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        networkDataHelper = Injector.provideNetworkDataHelper(this);

        /**
         * Menus
         */
        if(intent.getAction().equals(Constants.GET_MAIN_MENU_ITEMS)){
            networkDataHelper.startFetchMainMenuItems();
        }else if(intent.getAction().equals(GET_SUB_MENU_ITEMS)){
            String subMenuId = intent.getStringExtra(GET_SUB_MENU_ITEMS);
            networkDataHelper.startFetchSubMenuItems(subMenuId);
        }else if(intent.getAction().equals(GET_SUB_MENU_ITEMS_NO_CHILD)){
            networkDataHelper.startFetchSubMenuNoChildItems();
        }


        /**
         * Product
         */
        else if(intent.getAction().equals(GET_PRODUCTS_FOR_CATEGORY)){
            String categoyId = intent.getStringExtra(GET_PRODUCTS_FOR_CATEGORY);
            networkDataHelper.startFetchProductsForCategory(categoyId);
        }else if(intent.getAction().equals(GET_PRODUCTS_FOR_CATEGORY_AND_USER)){
            String categoyId = intent.getStringExtra(CATEGORY_ID);
            networkDataHelper.startFetchProductsForCategoryAndUser(categoyId);
        }else if(intent.getAction().equals(CHANGE_PRODUCT_STATUS)){
            String productId = intent.getStringExtra(PRODUCT_ID);
            boolean isEnable = intent.getBooleanExtra(IS_ENABLE, false);
            networkDataHelper.changeProductStatus(productId, isEnable);
        }else if(intent.getAction().equals(CHANGE_PRODUCT_PRICE)){
            String productId = intent.getStringExtra(PRODUCT_ID);
            String price = intent.getStringExtra(CHANGE_PRODUCT_PRICE);
            networkDataHelper.changeProductPrice(productId, price);
        }else if(intent.getAction().equals(START_EDIT_PRODUCT)){
            ProductItem productItem = intent.getParcelableExtra(START_EDIT_PRODUCT);
            String language = intent.getStringExtra(Constants.LANGUAGE);
            networkDataHelper.editProduct(productItem, language);
        }else if(intent.getAction().equals(START_ADD_NEW_PRODUCT)){
            ProductItem en = intent.getParcelableExtra(START_ADD_NEW_PRODUCT_EN_PRODUCT);
            ProductItem ar = intent.getParcelableExtra(START_ADD_NEW_PRODUCT_AR_PRODUCT);
            networkDataHelper.addNewProduct(en, ar);
        }else if(intent.getAction().equals(START_GET_PRODUCT)){
            String productId = intent.getStringExtra(START_GET_PRODUCT);
            String language = intent.getStringExtra(LANGUAGE);
            networkDataHelper.getProduct(productId, language);
        }else if(intent.getAction().equals(START_GET_PRODUCT_FOR_EDIT)){
            String productId = intent.getStringExtra(START_GET_PRODUCT_FOR_EDIT);
            networkDataHelper.getProductForEdit(productId);
        }




        /**
         * Cart
         */
        else if(intent.getAction().equals(GET_CART_ITEM)){
            String userId = intent.getStringExtra(GET_CART_ITEM);
            networkDataHelper.startFetchCartItem(userId);
        }else if(intent.getAction().equals(RESET_CART_ITEM)){
            networkDataHelper.resetFirebaseCart();
        }else if(intent.getAction().equals(START_DELETE_CART)){
            networkDataHelper.deleteAllFromCart();
        }

        /**
         * Users
         */

        else if(intent.getAction().equals(START_LOGIN)){
            String email = intent.getStringExtra(START_LOGIN_EMAIL);
            String password = intent.getStringExtra(START_LOGIN_PASSWORD);
            networkDataHelper.startFirebaseLogin(email,password);
        }else if(intent.getAction().equals(START_CREATE_NEW_ACCOUNT)){
            String firstname = intent.getStringExtra(START_CREATE_NEW_ACCOUNT_FIRSTNAME);
            String secondname = intent.getStringExtra(START_CREATE_NEW_ACCOUNT_SECONDNAME);
            String phone = intent.getStringExtra(START_CREATE_NEW_ACCOUNT_PHONE);
            String email = intent.getStringExtra(START_CREATE_NEW_ACCOUNT_EMAIL);
            String password = intent.getStringExtra(START_CREATE_NEW_ACCOUNT_PASSWORD);
            networkDataHelper.startCreateFirebaseAccount(firstname,secondname,phone,email,password);
        }else if(intent.getAction().equals(START_GET_CURRENT_USER_DETAILS)){
            String uid = intent.getStringExtra(START_GET_CURRENT_USER_DETAILS);
            networkDataHelper.fetchCurrentUserDetails(uid);
        }


        /**
         * Address
         */

        else if(intent.getAction().equals(START_CREATE_NEW_ADDRESS)){
            String userId = intent.getStringExtra(START_CREATE_NEW_ADDRESS);
            AddressItem addressItem = intent.getParcelableExtra(START_CREATE_NEW_ADDRESS_ADDRESS_ITEM);
            networkDataHelper.addNewAddress(userId, addressItem);
        }else if(intent.getAction().equals(START_GET_ADDRESSES)){
            String userId = intent.getStringExtra(START_GET_ADDRESSES);
            networkDataHelper.getAddressesFromFirebase(userId);
        }else if(intent.getAction().equals(START_GET_DEFAULT_ADDRESS)){
            String userId = intent.getStringExtra(START_GET_DEFAULT_ADDRESS);
            networkDataHelper.getDefaultAddressFromFirebase(userId);
        }else if(intent.getAction().equals(START_SET_DEFAULT_ADDRESS)){
            String id = intent.getStringExtra(START_SET_DEFAULT_ADDRESS);
            networkDataHelper.setDefaultAddress(id);
        }else if(intent.getAction().equals(START_DELETE_ADDRESS)){
            String id = intent.getStringExtra(START_DELETE_ADDRESS);
            networkDataHelper.deleteAddress(id);
        }else if(intent.getAction().equals(START_EDIT_ADDRESS)){
            AddressItem addressItem = intent.getParcelableExtra(START_EDIT_ADDRESS);
            networkDataHelper.editAddress(addressItem);
        }



        else if(intent.getAction().equals(START_GET_GOVERNORATES)){
            networkDataHelper.getGovernoratesFromFirebase();
        }


        /**
         * MarketPlace
         */
        else if(intent.getAction().equals(START_GET_MARKETPLACE)){
            String id = intent.getStringExtra(START_GET_MARKETPLACE);
            networkDataHelper.getMarketPlaceForId(id);
        }

        /**
         * Order
         */

        else if(intent.getAction().equals(START_NEW_ORDER)){
            OrderItem orderItem = intent.getParcelableExtra(START_NEW_ORDER);
            String marketId = intent.getStringExtra(MARKETPLACE_ID);
            networkDataHelper.startAddNewOrderToFirebase(orderItem, START_NEW_ORDER);
        }else if(intent.getAction().equals(START_GET_CURRENT_ORDERS)){
            networkDataHelper.startGetCurrentOrdersFromFirebase();
        }else if(intent.getAction().equals(START_REMOVE_ORDER)){
            String orderId = intent.getStringExtra(START_REMOVE_ORDER);
            networkDataHelper.removeOrderFromFireBase(orderId);
        }else if(intent.getAction().equals(START_REORDER)){
            OrderItem orderItem = intent.getParcelableExtra(START_REORDER);
            networkDataHelper.startAddNewOrderToFirebase(orderItem, START_REORDER);
        }else if(intent.getAction().equals(CHANGE_ORDER_STATUS)){
            String orderId = intent.getStringExtra(ORDER_ID);
            int orderStatus = intent.getIntExtra(ORDER_STATUS,0);
            networkDataHelper.changeOrderStatusFirebase(orderId, orderStatus);
        }else if(intent.getAction().equals(START_GET_USER_FOR_ORDER)){
            String userId = intent.getStringExtra(START_GET_USER_FOR_ORDER);
            networkDataHelper.getUserForOrder(userId);
        }else if(intent.getAction().equals(START_GET_ORDER)){
            String orderId = intent.getStringExtra(START_GET_ORDER);
            networkDataHelper.getOrderById(orderId);
        }


        else if(intent.getAction().equals(START_GET_FAVORITES)){
            networkDataHelper.getFavoritesFromFirebase();
        }


        else if(intent.getAction().equals(START_GET_BANNER)){
            networkDataHelper.getBannerFromFirebase();
        }

        /**
         * Seller
         */

        else if(intent.getAction().equals(SELLER_GET_ORDERS)){
            String marketId = intent.getStringExtra(MARKETPLACE_ID);
            networkDataHelper.getSellerOrders(marketId);
        }else if(intent.getAction().equals(START_GET_SELLER_MARKETPLACE)){
            String uid = intent.getStringExtra(EXTRA_USER_ID);
            networkDataHelper.getMarketPlacesForSeller(uid);
        }


    }
}
