package online.lahloba.www.lahloba.ui.products.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.databinding.BottomSheetResetCartBinding;

public class ResetCartBottomSheet extends BottomSheetDialogFragment {
    ResetCartListener resetCartListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetResetCartBinding binding = BottomSheetResetCartBinding.inflate(inflater,container,false);
        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCartListener.onResetCartItemClicked(v.getId());
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCartListener.onResetCartItemClicked(v.getId());
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            resetCartListener = (ResetCartListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Class must implement ResetCartListener interface");
        }
    }

    public interface ResetCartListener{
        void onResetCartItemClicked(int id);
    }
}
