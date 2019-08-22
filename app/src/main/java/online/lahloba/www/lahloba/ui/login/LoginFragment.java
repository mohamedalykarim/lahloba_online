package online.lahloba.www.lahloba.ui.login;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.databinding.FragmentLoginBinding;
import online.lahloba.www.lahloba.ui.signup.SignupActivity;
import online.lahloba.www.lahloba.utils.Injector;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    TextView phoneET, passwordET;
    FragmentLoginBinding binding;
    int error = 0;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false);
        binding.setLifecycleOwner(this);
        phoneET = binding.phoneET;
        passwordET = binding.passwordET;

        createAccount();
        login();



        View view = binding.getRoot();
        return view;
    }

    private void login() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkloginInput(phoneET.getText().toString(), passwordET.getText().toString());
                if (error==0){
                    String email = "lahloba"+phoneET.getText().toString()+"@lahloba.net";
                    mViewModel.startLogin(email, passwordET.getText().toString());
                    binding.loginProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkloginInput(String phone, String password) {
        if (phone.length() > 0 && phone.length() < 10 ){
            phoneET.setError("Phone length must be between 10 to 13 character");
            error++;
        }

        if (phone.length() > 13 ){
            phoneET.setError("Phone length must be between 10 to 13 character");
            error++;
        }



        if (password.equals("") || null == password){
            passwordET.setError("Please Enter Valid Password");
            error++;
        }


        if (password.length() > 0 && password.length() < 6 ){
            passwordET.setError("Password length must be between 6 to 10 character");
            error++;
        }

        if (password.length() > 10 ){
            passwordET.setError("Password length must be between 6 to 10 character");
            error++;
        }

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this.getContext());
        mViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);

        mViewModel.getIsLogged().observe(this, isLogged->{
            if (isLogged){
                mViewModel.deleteLocalCartItems();
                Toast.makeText(this.getContext(), "Welcome Back", Toast.LENGTH_SHORT).show();
                binding.loginProgressBar.setVisibility(View.INVISIBLE);

                if (getActivity() instanceof LoginActivity){
                    getActivity().finish();
                }

            }
        });
    }

    public void createAccount(){
        binding.newAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }





}
