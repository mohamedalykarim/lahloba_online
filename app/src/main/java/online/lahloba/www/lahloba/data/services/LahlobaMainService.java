package online.lahloba.www.lahloba.data.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.data.model.CartItem;
import online.lahloba.www.lahloba.data.model.OrderItem;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.GET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.RESET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT;
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
import static online.lahloba.www.lahloba.utils.Constants.START_GET_BANNER;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_ORDERS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_USER_DETAILS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_DEFAULT_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_FAVORITES;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_GOVERNORATES;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_MARKETPLACE;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_NEW_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_REMOVE_ORDER;
import static online.lahloba.www.lahloba.utils.Constants.START_SET_DEFAULT_ADDRESS;

public class LahlobaMainService extends IntentService {
    private NetworkDataHelper networkDataHelper;

    public LahlobaMainService() {
        super("LahlobaMainService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        networkDataHelper = Injector.provideNetworkDataHelper(this);
        if(intent.getAction().equals(Constants.GET_MAIN_MENU_ITEMS)){
            networkDataHelper.startFetchMainMenuItems();
        }else if(intent.getAction().equals(GET_SUB_MENU_ITEMS)){
            String subMenuId = intent.getStringExtra(GET_SUB_MENU_ITEMS);
            networkDataHelper.startFetchSubMenuItems(subMenuId);
        }else if(intent.getAction().equals(GET_PRODUCTS_FOR_CATEGORY)){
            String categoyId = intent.getStringExtra(GET_PRODUCTS_FOR_CATEGORY);
            networkDataHelper.startFetchProductsForCategory(categoyId);
        }else if(intent.getAction().equals(GET_CART_ITEM)){
            String userId = intent.getStringExtra(GET_CART_ITEM);
            networkDataHelper.startFetchCartItem(userId);
        }else if(intent.getAction().equals(START_LOGIN)){
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
        }else if(intent.getAction().equals(START_CREATE_NEW_ADDRESS)){
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
        }else if(intent.getAction().equals(START_GET_MARKETPLACE)){
            String id = intent.getStringExtra(START_GET_MARKETPLACE);
            networkDataHelper.getMarketPlaceForId(id);
        }else if(intent.getAction().equals(START_NEW_ORDER)){
            OrderItem orderItem = intent.getParcelableExtra(START_NEW_ORDER);
            networkDataHelper.startAddNewOrderToFirebase(orderItem);
        }else if(intent.getAction().equals(START_GET_CURRENT_ORDERS)){
            networkDataHelper.startGetCurrentOrdersFromFirebase();
        }else if(intent.getAction().equals(START_REMOVE_ORDER)){
            String orderId = intent.getStringExtra(START_REMOVE_ORDER);
            networkDataHelper.removeOrderFromFireBase(orderId);
        }else if(intent.getAction().equals(RESET_CART_ITEM)){
            networkDataHelper.resetFirebaseCart();
        }


        else if(intent.getAction().equals(START_GET_FAVORITES)){
            networkDataHelper.getFavoritesFromFirebase();
        }


        else if(intent.getAction().equals(START_GET_BANNER)){
            networkDataHelper.getBannerFromFirebase();
        }



    }
}
