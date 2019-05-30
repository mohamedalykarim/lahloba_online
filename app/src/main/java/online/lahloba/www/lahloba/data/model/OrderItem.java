package online.lahloba.www.lahloba.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderItem implements Parcelable {
    String id;
    private double total;
    private AddressItem addressSelected;
    private String shippingMethodSelected;
    private double hyperlocalCost = 0;
    private String pay_method;
    private HashMap<String, CartItem> products;
    private double orderTotal;

    public OrderItem() {
    }

    protected OrderItem(Parcel in) {
        id = in.readString();
        total = in.readDouble();
        addressSelected = in.readParcelable(AddressItem.class.getClassLoader());
        shippingMethodSelected = in.readString();
        hyperlocalCost = in.readDouble();
        pay_method = in.readString();
        orderTotal = in.readDouble();
        products = new HashMap<>();
        in.readMap(products, OrderItem.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(total);
        dest.writeParcelable(addressSelected, flags);
        dest.writeString(shippingMethodSelected);
        dest.writeDouble(hyperlocalCost);
        dest.writeString(pay_method);
        dest.writeDouble(orderTotal);
        dest.writeMap(products);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public double getTotal() {
        return total;
    }

    public AddressItem getAddressSelected() {
        return addressSelected;
    }

    public String getShippingMethodSelected() {
        return shippingMethodSelected;
    }

    public double getHyperlocalCost() {
        return hyperlocalCost;
    }

    public String getPay_method() {
        return pay_method;
    }

    public HashMap<String, CartItem> getProducts() {
        return products;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setAddressSelected(AddressItem addressSelected) {
        this.addressSelected = addressSelected;
    }

    public void setShippingMethodSelected(String shippingMethodSelected) {
        this.shippingMethodSelected = shippingMethodSelected;
    }

    public void setHyperlocalCost(double hyperlocalCost) {
        this.hyperlocalCost = hyperlocalCost;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public void setProducts(HashMap<String, CartItem> products) {
        this.products = products;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }
}
