package online.lahloba.www.lahloba.utils.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ProductOptionComperatorByValue implements Comparator<String> {
    Map<String, String> option;

    public ProductOptionComperatorByValue(Map<String, String> option) {
        this.option = option;
    }

    @Override
    public int compare(String o1, String o2) {

        if (Double.parseDouble(option.get(o1)) >= Double.parseDouble(option.get(o2))){
            return 1;
        }else {
            return -1;
        }

    }
}
