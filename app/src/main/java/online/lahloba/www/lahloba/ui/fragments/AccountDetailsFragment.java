package online.lahloba.www.lahloba.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.auth.FirebaseAuth;

import online.lahloba.www.lahloba.databinding.FragmentAccountDetailsBinding;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;

public class AccountDetailsFragment extends DialogFragment {
    int Error = 0;
    LoginViewModel loginViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountDetailsBinding binding = FragmentAccountDetailsBinding
                .inflate(inflater, container, false);

        loginViewModel.startGetUserDetails(FirebaseAuth.getInstance().getUid());

        loginViewModel.getCurrentUserDetails().observe((LifecycleOwner) getContext(), userItem -> {
            if (userItem == null)return;

            binding.setUserDetails(userItem);

        });


        binding.editDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.startUpdateUserDetails(binding.getUserDetails());
                dismiss();
            }
        });

        return binding.getRoot();
    }


    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
