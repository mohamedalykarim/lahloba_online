package online.lahloba.www.lahloba.ui.cart.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.databinding.BottomSheetShippingBinding;

public class ShippingMethodBottomSheet extends BottomSheetDialogFragment {
    OnShippingMethodClicked onShippingMethodClicked;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetShippingBinding binding = BottomSheetShippingBinding.inflate(inflater,container,false);
        binding.freeShippingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShippingMethodClicked.onShippingMethodClicked(v.getId());
            }
        });

        binding.hyperLocalShippingBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShippingMethodClicked.onShippingMethodClicked(v.getId());
            }
        });
        
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onShippingMethodClicked = (OnShippingMethodClicked) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must implement OnLoginSheetClicked");
        }

    }



    public interface OnShippingMethodClicked{
        void onShippingMethodClicked(int id);
    }
}
