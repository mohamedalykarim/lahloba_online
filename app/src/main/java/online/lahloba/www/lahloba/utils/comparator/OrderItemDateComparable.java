package online.lahloba.www.lahloba.utils.comparator;

import java.util.Comparator;

import online.lahloba.www.lahloba.data.model.OrderItem;

public class OrderItemDateComparable implements Comparator<OrderItem> {
    @Override
    public int compare(OrderItem o1, OrderItem o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
