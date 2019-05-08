package online.lahloba.www.lahloba.data.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.GET_CART_ITEM;
import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_FIRSTNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PASSWORD;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_PHONE;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ACCOUNT_SECONDNAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_BUILDING;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_CITY;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_COUNTRY;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_FLAT;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_FLOOR;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_NAME;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_STREET;
import static online.lahloba.www.lahloba.utils.Constants.START_CREATE_NEW_ADDRESS_ZONE;
import static online.lahloba.www.lahloba.utils.Constants.START_GET_CURRENT_USER_DETAILS;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_EMAIL;
import static online.lahloba.www.lahloba.utils.Constants.START_LOGIN_PASSWORD;

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
            String name = intent.getStringExtra(START_CREATE_NEW_ADDRESS_NAME);
            String country = intent.getStringExtra(START_CREATE_NEW_ADDRESS_COUNTRY);
            String city = intent.getStringExtra(START_CREATE_NEW_ADDRESS_CITY);
            String zone = intent.getStringExtra(START_CREATE_NEW_ADDRESS_ZONE);
            String street = intent.getStringExtra(START_CREATE_NEW_ADDRESS_STREET);
            String building = intent.getStringExtra(START_CREATE_NEW_ADDRESS_BUILDING);
            int floor = intent.getIntExtra(START_CREATE_NEW_ADDRESS_FLOOR,0);
            int flat = intent.getIntExtra(START_CREATE_NEW_ADDRESS_FLAT,0);

            networkDataHelper.AddNewAddress(userId, name,country, city, zone, street, building, floor, flat);
        }
    }
}
