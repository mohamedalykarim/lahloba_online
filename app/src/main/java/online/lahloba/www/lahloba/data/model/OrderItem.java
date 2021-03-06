package online.lahloba.www.lahloba.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;

import online.lahloba.www.lahloba.BR;

public class OrderItem extends BaseObservable implements Parcelable {
    private String id;
    private double total;
    private AddressItem addressSelected;
    private String shippingMethodSelected;
    private double hyperlocalCost = 0;
    private String pay_method;
    private HashMap<String, CartItem> products;
    private double orderTotal;
    private int orderStatus;
    private long orderNumber;
    private Date date;
    private String marketplaceId;
    private String userId;
    private String cityId;
    private String cityIdStatus;
    private String deliveryAllocatedTo;

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
        orderStatus = in.readInt();
        orderNumber = in.readLong();
        date = (Date) in.readSerializable();
        marketplaceId = in.readString();
        userId = in.readString();
        cityId = in.readString();
        cityIdStatus = in.readString();
        deliveryAllocatedTo = in.readString();
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
        dest.writeInt(orderStatus);
        dest.writeLong(orderNumber);
        dest.writeSerializable(date);
        dest.writeString(marketplaceId);
        dest.writeString(userId);
        dest.writeString(cityId);
        dest.writeString(cityIdStatus);
        dest.writeString(deliveryAllocatedTo);
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

    public int getOrderStatus() {
        return orderStatus;
    }

    @Bindable
    public long getOrderNumber() {
        return orderNumber;
    }

    public Date getDate() {
        return date;
    }

    public String getMarketplaceId() {
        return marketplaceId;
    }

    public String getUserId() {
        return userId;
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

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
        notifyPropertyChanged(BR.orderNumber);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMarketplaceId(String marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityIdStatus() {
        return cityIdStatus;
    }

    public void setCityIdStatus(String cityIdStatus) {
        this.cityIdStatus = cityIdStatus;
    }

    public String getDeliveryAllocatedTo() {
        return deliveryAllocatedTo;
    }

    public void setDeliveryAllocatedTo(String deliveryAllocatedTo) {
        this.deliveryAllocatedTo = deliveryAllocatedTo;
    }
}
