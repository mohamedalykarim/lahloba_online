package online.lahloba.www.lahloba.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import online.lahloba.www.lahloba.ui.products.ProductsActivity;

public class Utils {

    public static boolean checkValidEmail(String email){
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(mat.matches()){
            return true;
        }else{
            return false;
        }

    }

    public static int getCostByDistance(double distance){
        if (distance <= 5){
            return 10;
        }else if (distance >5 && distance <= 10){
            return 15;
        }else if (distance >10 && distance <= 15){
            return 20;
        }else if (distance >15 && distance <= 20){
            return 25;
        }else if (distance > 20){
            return 30;
        }

        return 0;

    }


    public static String getBannerActivityName(String type){
        if (type.equals(Constants.BANNER_TYPE_PRODUCTS_CATEGORY)){
            return ".ui.products.ProductsActivity";
        }else if (type.equals(Constants.BANNER_TYPE_NEW_NEWS)){
            return ".ui.news.NewsActivity";
        }else {
            return "";
        }
    }

}
