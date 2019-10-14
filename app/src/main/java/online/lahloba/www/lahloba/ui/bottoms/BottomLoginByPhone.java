package online.lahloba.www.lahloba.ui.bottoms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.databinding.BottomLoginByPhoneBinding;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;

public class BottomLoginByPhone extends BottomSheetDialogFragment {
    BottomLoginByPhoneBinding binding;
    LoginViewModel loginViewModel;


    String phoneNumber;
    private String codeSent;

    private int errors;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null){
                confirmPhone(code);
                binding.confirmationET.setText(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomLoginByPhoneBinding.inflate(inflater, container, false);

        phoneNumber = "+2"+phoneNumber;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                    TimeUnit.SECONDS,
                getActivity(),
                callbacks
        );

        binding.confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateConfirmationCode();
                if (errors > 0) return;

                confirmPhone(binding.confirmationET.getText().toString());
            }
        });

        return binding.getRoot();
    }

    private void validateConfirmationCode() {
        errors = 0;
        if (binding.confirmationET.getText().toString().equals("")){
            binding.confirmationET.setError("Please enter confirmation code first.");
            errors = errors+1;
        }
    }

    private void confirmPhone(String verificationCode) {
        if (verificationCode == null) return;

        if (codeSent == null) return;

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,verificationCode);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginViewModel.startLoggedByPhone();
                            loginViewModel.setIsLogged(true);
                            dismiss();
                        } else {
                                // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });

    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
