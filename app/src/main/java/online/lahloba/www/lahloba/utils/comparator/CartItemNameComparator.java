package online.lahloba.www.lahloba.utils.comparator;

import java.util.Comparator;

import online.lahloba.www.lahloba.data.model.CartItem;

public class CartItemNameComparator implements Comparator<CartItem> {
    @Override
    public int compare(CartItem o1, CartItem o2) {
        return o1.getProductName().compareTo(o2.getProductName());
    }
}
