package online.lahloba.www.lahloba.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class ProductOption implements Parcelable {
    String optionId;
    String optionKey;
    String optionValue;

    public ProductOption() {
    }

    protected ProductOption(Parcel in) {
        optionId = in.readString();
        optionKey = in.readString();
        optionValue = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(optionId);
        dest.writeString(optionKey);
        dest.writeString(optionValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductOption> CREATOR = new Creator<ProductOption>() {
        @Override
        public ProductOption createFromParcel(Parcel in) {
            return new ProductOption(in);
        }

        @Override
        public ProductOption[] newArray(int size) {
            return new ProductOption[size];
        }
    };

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(String optionKey) {
        this.optionKey = optionKey;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }
}
