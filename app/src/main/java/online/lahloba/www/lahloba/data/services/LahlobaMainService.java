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
        }
    }
}
