package online.lahloba.www.lahloba.data.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import online.lahloba.www.lahloba.data.NetworkDataHelper;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.GET_PRODUCTS_FOR_CATEGORY;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;

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
        }
    }
}
