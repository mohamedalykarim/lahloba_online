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

import online.lahloba.www.lahloba.databinding.FragmentPointsBinding;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;

public class PointsFragment extends DialogFragment {

    LoginViewModel loginViewModel;
    private FragmentPointsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPointsBinding.inflate(inflater,container, false);

        loginViewModel.startGetUserDetails(FirebaseAuth.getInstance().getUid());

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();


        loginViewModel.getCurrentUserDetails().observe((LifecycleOwner) getContext(), userItem -> {
            binding.setPoint(userItem.getPoints()+"");
        });

    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
