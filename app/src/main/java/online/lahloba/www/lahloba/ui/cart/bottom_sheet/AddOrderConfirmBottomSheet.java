package online.lahloba.www.lahloba.ui.cart.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.databinding.BottomSheetAddOrderBinding;

public class AddOrderConfirmBottomSheet extends BottomSheetDialogFragment {
    ConfirmClickListener confirmClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetAddOrderBinding binding = BottomSheetAddOrderBinding.inflate(inflater, container, false);
        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClickListener.onClickConfirmItem(v.getId());
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClickListener.onClickConfirmItem(v.getId());
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            confirmClickListener = (ConfirmClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Class must implement ConfirmClickListener interface");
        }
    }

    public interface ConfirmClickListener{
        void onClickConfirmItem(int id);
    }
}
