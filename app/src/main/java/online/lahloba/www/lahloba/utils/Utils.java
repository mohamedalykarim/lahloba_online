package online.lahloba.www.lahloba.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
