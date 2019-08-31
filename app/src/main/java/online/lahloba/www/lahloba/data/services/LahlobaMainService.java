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
        }else if(intent.getAction().equals(Constants.GET_SUB_MENU_ITEMS)){
            String subMenuId = intent.getStringExtra(Constants.GET_SUB_MENU_ITEMS);
            networkDataHelper.startFetchSubMenuItems(subMenuId);
        }else if(intent.getAction().equals(Constants.GET_SUB_MENU_ITEMS_NO_CHILD)){
            networkDataHelper.startFetchSubMenuNoChildItems();
        }


        /**
         * Product
         */
        else if(intent.getAction().equals(Constants.GET_PRODUCTS_FOR_CATEGORY)){
            String categoyId = intent.getStringExtra(Constants.GET_PRODUCTS_FOR_CATEGORY);
            networkDataHelper.startFetchProductsForCategory(categoyId);
        }else if(intent.getAction().equals(Constants.GET_PRODUCTS_FOR_CATEGORY_AND_USER)){
            String categoyId = intent.getStringExtra(Constants.CATEGORY_ID);
            networkDataHelper.startFetchProductsForCategoryAndUser(categoyId);
        }else if(intent.getAction().equals(Constants.CHANGE_PRODUCT_STATUS)){
            String productId = intent.getStringExtra(Constants.PRODUCT_ID);
            boolean isEnable = intent.getBooleanExtra(Constants.IS_ENABLE, false);
            networkDataHelper.changeProductStatus(productId, isEnable);
        }else if(intent.getAction().equals(Constants.CHANGE_PRODUCT_PRICE)){
            String productId = intent.getStringExtra(Constants.PRODUCT_ID);
            String price = intent.getStringExtra(Constants.CHANGE_PRODUCT_PRICE);
            networkDataHelper.changeProductPrice(productId, price);
        }else if(intent.getAction().equals(Constants.START_EDIT_PRODUCT)){
            ProductItem productItem = intent.getParcelableExtra(Constants.START_EDIT_PRODUCT);
            String language = intent.getStringExtra(Constants.LANGUAGE);
            networkDataHelper.editProduct(productItem, language);
        }else if(intent.getAction().equals(Constants.START_ADD_NEW_PRODUCT)){
            ProductItem en = intent.getParcelableExtra(Constants.START_ADD_NEW_PRODUCT_EN_PRODUCT);
            ProductItem ar = intent.getParcelableExtra(Constants.START_ADD_NEW_PRODUCT_AR_PRODUCT);
            networkDataHelper.addNewProduct(en, ar);
        }else if(intent.getAction().equals(Constants.START_GET_PRODUCT)){
            String productId = intent.getStringExtra(Constants.START_GET_PRODUCT);
            String language = intent.getStringExtra(Constants.LANGUAGE);
            networkDataHelper.getProduct(productId, language);
        }else if(intent.getAction().equals(Constants.START_GET_PRODUCT_FOR_EDIT)){
            String productId = intent.getStringExtra(Constants.START_GET_PRODUCT_FOR_EDIT);
            networkDataHelper.getProductForEdit(productId);
        }




        /**
         * Cart
         */
        else if(intent.getAction().equals(Constants.GET_CART_ITEM)){
            String userId = intent.getStringExtra(Constants.GET_CART_ITEM);
            networkDataHelper.startFetchCartItem(userId);
        }else if(intent.getAction().equals(Constants.RESET_CART_ITEM)){
            networkDataHelper.resetFirebaseCart();
        }else if(intent.getAction().equals(Constants.START_DELETE_CART)){
            networkDataHelper.deleteAllFromCart();
        }

        /**
         * Users
         */

        else if(intent.getAction().equals(Constants.START_LOGIN)){
            String email = intent.getStringExtra(Constants.START_LOGIN_EMAIL);
            String password = intent.getStringExtra(Constants.START_LOGIN_PASSWORD);
            networkDataHelper.startFirebaseLogin(email,password);
        }else if(intent.getAction().equals(Constants.START_CREATE_NEW_ACCOUNT)){
            String firstname = intent.getStringExtra(Constants.START_CREATE_NEW_ACCOUNT_FIRSTNAME);
            String secondname = intent.getStringExtra(Constants.START_CREATE_NEW_ACCOUNT_SECONDNAME);
            String phone = intent.getStringExtra(Constants.START_CREATE_NEW_ACCOUNT_PHONE);
            String email = intent.getStringExtra(Constants.START_CREATE_NEW_ACCOUNT_EMAIL);
            String password = intent.getStringExtra(Constants.START_CREATE_NEW_ACCOUNT_PASSWORD);
            networkDataHelper.startCreateFirebaseAccount(firstname,secondname,phone,email,password);
        }else if(intent.getAction().equals(Constants.START_GET_CURRENT_USER_DETAILS)){
            String uid = intent.getStringExtra(Constants.START_GET_CURRENT_USER_DETAILS);
            networkDataHelper.fetchCurrentUserDetails(uid);
        }


        /**
         * Address
         */

        else if(intent.getAction().equals(Constants.START_CREATE_NEW_ADDRESS)){
            String userId = intent.getStringExtra(Constants.START_CREATE_NEW_ADDRESS);
            AddressItem addressItem = intent.getParcelableExtra(Constants.START_CREATE_NEW_ADDRESS_ADDRESS_ITEM);
            networkDataHelper.addNewAddress(userId, addressItem);
        }else if(intent.getAction().equals(Constants.START_GET_ADDRESSES)){
            String userId = intent.getStringExtra(Constants.START_GET_ADDRESSES);
            networkDataHelper.getAddressesFromFirebase(userId);
        }else if(intent.getAction().equals(Constants.START_GET_DEFAULT_ADDRESS)){
            String userId = intent.getStringExtra(Constants.START_GET_DEFAULT_ADDRESS);
            networkDataHelper.getDefaultAddressFromFirebase(userId);
        }else if(intent.getAction().equals(Constants.START_SET_DEFAULT_ADDRESS)){
            String id = intent.getStringExtra(Constants.START_SET_DEFAULT_ADDRESS);
            networkDataHelper.setDefaultAddress(id);
        }else if(intent.getAction().equals(Constants.START_DELETE_ADDRESS)){
            String id = intent.getStringExtra(Constants.START_DELETE_ADDRESS);
            networkDataHelper.deleteAddress(id);
        }else if(intent.getAction().equals(Constants.START_EDIT_ADDRESS)){
            AddressItem addressItem = intent.getParcelableExtra(Constants.START_EDIT_ADDRESS);
            networkDataHelper.editAddress(addressItem);
        }



        else if(intent.getAction().equals(Constants.START_GET_GOVERNORATE)){
            networkDataHelper.getGovernorateFromFirebase();
        }else if(intent.getAction().equals(Constants.START_GET_CITIES)){
            networkDataHelper.getCitiesFromFirebase();
        }


        /**
         * MarketPlace
         */
        else if(intent.getAction().equals(Constants.START_GET_MARKETPLACE)){
            String id = intent.getStringExtra(Constants.START_GET_MARKETPLACE);
            networkDataHelper.getMarketPlaceForId(id);
        }

        /**
         * Order
         */

        else if(intent.getAction().equals(Constants.START_NEW_ORDER)){
            OrderItem orderItem = intent.getParcelableExtra(Constants.START_NEW_ORDER);
            String marketId = intent.getStringExtra(Constants.MARKETPLACE_ID);
            networkDataHelper.startAddNewOrderToFirebase(orderItem, Constants.START_NEW_ORDER);
        }else if(intent.getAction().equals(Constants.START_GET_CURRENT_ORDERS)){
            networkDataHelper.startGetCurrentOrdersFromFirebase();
        }else if(intent.getAction().equals(Constants.START_REMOVE_ORDER)){
            String orderId = intent.getStringExtra(Constants.START_REMOVE_ORDER);
            networkDataHelper.removeOrderFromFireBase(orderId);
        }else if(intent.getAction().equals(Constants.START_REORDER)){
            OrderItem orderItem = intent.getParcelableExtra(Constants.START_REORDER);
            networkDataHelper.startAddNewOrderToFirebase(orderItem, Constants.START_REORDER);
        }else if(intent.getAction().equals(Constants.CHANGE_ORDER_STATUS)){
            String orderId = intent.getStringExtra(Constants.ORDER_ID);
            String cityId = intent.getStringExtra(Constants.CITY_ID);
            int orderStatus = intent.getIntExtra(Constants.ORDER_STATUS,0);
            networkDataHelper.changeOrderStatusFirebase(orderId, cityId, orderStatus);
        }else if(intent.getAction().equals(Constants.START_GET_USER_FOR_ORDER)){
            String userId = intent.getStringExtra(Constants.START_GET_USER_FOR_ORDER);
            networkDataHelper.getUserForOrder(userId);
        }else if(intent.getAction().equals(Constants.START_GET_ORDER)){
            String orderId = intent.getStringExtra(Constants.START_GET_ORDER);
            networkDataHelper.getOrderById(orderId);
        }else if(intent.getAction().equals(Constants.START_UPDATE_ORDER)){
            OrderItem orderItem = intent.getParcelableExtra(Constants.START_UPDATE_ORDER);
            networkDataHelper.updateOrder(orderItem);
        }


        else if(intent.getAction().equals(Constants.START_GET_FAVORITES)){
            networkDataHelper.getFavoritesFromFirebase();
        }


        else if(intent.getAction().equals(Constants.START_GET_BANNER)){
            networkDataHelper.getBannerFromFirebase();
        }

        /**
         * Seller
         */

        else if(intent.getAction().equals(Constants.SELLER_GET_ORDERS)){
            String marketId = intent.getStringExtra(Constants.MARKETPLACE_ID);
            networkDataHelper.getSellerOrders(marketId);
        }else if(intent.getAction().equals(Constants.START_GET_SELLER_MARKETPLACE)){
            String uid = intent.getStringExtra(Constants.EXTRA_USER_ID);
            networkDataHelper.getMarketPlacesForSeller(uid);
        }


        /**
         * Delivery
         */

        else if(intent.getAction().equals(Constants.START_GET_DELIVERY_AREAS)){
            int areaType = intent.getIntExtra(Constants.START_GET_DELIVERY_AREAS,0);
            networkDataHelper.getDeliveryAreasFromFirebase(areaType);
        }else if(intent.getAction().equals(Constants.START_GET_ORDERS_FOR_DELIVERY_SUPERVISOR)){
            String cityId = intent.getStringExtra(Constants.START_GET_ORDERS_FOR_DELIVERY_SUPERVISOR);
            networkDataHelper.getOrdersForDeliverysupervisor(cityId);
        }else if(intent.getAction().equals(Constants.START_GET_DELIVERIES_FOR_CITY)){
            String cityId = intent.getStringExtra(Constants.CITY_ID);
            int areaType = intent.getIntExtra(Constants.DELIVERY_AREA_TYPE,1);
            networkDataHelper.getDeliveriesForCity(cityId, areaType);
        }else if(intent.getAction().equals(Constants.START_GET_ORDERS_FOR_DELIVERY)){
            networkDataHelper.getOrdersForDelivery();
        }


    }
}
