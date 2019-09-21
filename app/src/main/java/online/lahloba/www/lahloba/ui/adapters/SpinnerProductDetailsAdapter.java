package online.lahloba.www.lahloba.ui.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class SpinnerProductDetailsAdapter extends ArrayAdapter {
    List<String> keys;
    List<String> values;


    public SpinnerProductDetailsAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }


    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
