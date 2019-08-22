package online.lahloba.www.lahloba.ui.cart.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.databinding.BottomSheetLoginBinding;

public class LogginBottomSheet extends BottomSheetDialogFragment {
    OnLoginSheetClicked onLoginSheetClicked;
    BottomSheetLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         binding = BottomSheetLoginBinding.inflate(inflater,container,false);
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginSheetClicked.onLoginSheetItemClicked(v.getId());
            }
        });

        binding.googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginSheetClicked.onLoginSheetItemClicked(v.getId());
            }
        });

        return binding.getRoot();
    }


    public interface OnLoginSheetClicked{
        void onLoginSheetItemClicked(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onLoginSheetClicked = (OnLoginSheetClicked) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must implement OnLoginSheetClicked");
        }

    }
}
