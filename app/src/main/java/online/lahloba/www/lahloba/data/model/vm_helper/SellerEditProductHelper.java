package online.lahloba.www.lahloba.data.model.vm_helper;

import android.graphics.Bitmap;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import online.lahloba.www.lahloba.BR;
import online.lahloba.www.lahloba.data.model.ProductItem;

public class SellerEditProductHelper extends BaseObservable {
    ProductItem arProduct;
    ProductItem enProduct;
    int error;
    Bitmap bitmap;


    @Bindable
    public ProductItem getArProduct() {
        return arProduct;
    }

    @Bindable
    public void setArProduct(ProductItem arProduct) {
        this.arProduct = arProduct;
        notifyPropertyChanged(BR.arProduct);
    }
    @Bindable
    public ProductItem getEnProduct() {
        return enProduct;
    }
    @Bindable
    public void setEnProduct(ProductItem enProduct) {
        this.enProduct = enProduct;
        notifyPropertyChanged(BR.enProduct);

    }
    @Bindable
    public int getError() {
        return error;
    }
    @Bindable
    public void setError(int error) {
        this.error = error;
        notifyPropertyChanged(BR.error);
    }
    @Bindable
    public Bitmap getBitmap() {
        return bitmap;
    }
    @Bindable
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        notifyPropertyChanged(BR.bitmap);
    }
}
