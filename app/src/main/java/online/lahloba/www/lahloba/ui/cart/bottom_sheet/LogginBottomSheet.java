package online.lahloba.www.lahloba.ui.cart.bottom_sheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import online.lahloba.www.lahloba.databinding.BottomSheetLoginBinding;
import online.lahloba.www.lahloba.ui.login.LoginActivity;

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
