package online.lahloba.www.lahloba.data.model.vm_helper;

import android.graphics.Bitmap;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import online.lahloba.www.lahloba.BR;
import online.lahloba.www.lahloba.data.model.ProductItem;

public class SellerAddProductHelper extends BaseObservable {
    int error = 0;
    String categoryId;

    Bitmap myBitmap;
    public String fStepEnName;
    public String fStepArName;
    public String fStepPrice;

    public SellerAddProductHelper() {
    }



    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    public void setMyBitmap(Bitmap myBitmap) {
        this.myBitmap = myBitmap;
    }

    @Bindable
    public String getfStepEnName() {
        return fStepEnName;
    }

    @Bindable
    public void setfStepEnName(String fStepEnName) {
        this.fStepEnName = fStepEnName;
        notifyPropertyChanged(BR.fStepEnName);

    }

    @Bindable
    public String getfStepArName() {
        return fStepArName;
    }

    @Bindable
    public void setfStepArName(String fStepArName) {
        this.fStepArName = fStepArName;
        notifyPropertyChanged(BR.fStepArName);
    }

    @Bindable
    public String getfStepPrice() {
        return fStepPrice;
    }

    @Bindable
    public void setfStepPrice(String fStepPrice) {
        this.fStepPrice = fStepPrice;
        notifyPropertyChanged(BR.fStepPrice);
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
