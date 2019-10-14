package online.lahloba.www.lahloba.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.UserItem;
import online.lahloba.www.lahloba.databinding.FragmentLoginMenuBinding;
import online.lahloba.www.lahloba.ui.bottoms.BottomLoginByPhone;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;

import static android.app.Activity.RESULT_OK;

public class LoginMenuFragment extends Fragment {

    private static final int RC_SIGN_IN = 100;
    private int errors = 0;

    FragmentLoginMenuBinding binding;
    private LoginViewModel loginViewModel;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginMenuBinding.inflate(inflater,container, false);

        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext());
        loginViewModel = ViewModelProviders.of(this,factory).get(LoginViewModel.class);

        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);



        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePhoneNumber();
                if (errors > 0)return;

                BottomLoginByPhone bottomLoginByPhone = new BottomLoginByPhone();
                bottomLoginByPhone.setPhoneNumber(binding.phoneET.getText().toString());
                bottomLoginByPhone.setLoginViewModel(loginViewModel);
                bottomLoginByPhone.show(getActivity().getSupportFragmentManager(), "BottomLoginByPhone");
            }
        });

        binding.googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        return binding.getRoot();
    }

    private void validatePhoneNumber() {
        errors = 0;
        if (binding.phoneET.getText().toString().equals("")){
            binding.phoneET.setError("Please enter phone number.");
            errors = errors + 1;
        }else if(binding.phoneET.length() < 11){
            binding.phoneET.setError("phone number length must be 11 digit");
            errors += errors + 1;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);



                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    // [START_EXCLUDE]
                }
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            loginViewModel.loginVMHelper.setLogged(true);


                            String familyName = acct.getFamilyName();
                            String firstName = user.getDisplayName().replace(familyName,"");

                            UserItem userItem = new UserItem();
                            userItem.setEmail(user.getEmail());
                            userItem.setFirstName(firstName);
                            userItem.setLastName(familyName);
                            userItem.setId(FirebaseAuth.getInstance().getUid());
                            userItem.setSeller(false);
                            userItem.setStatus(true);
                            userItem.setDelivery(false);
                            userItem.setDeliverySupervisor(false);

                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                            mRef.child("User").child(FirebaseAuth.getInstance().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.exists()){
                                                mRef.child("User").child(userItem.getId()).setValue(userItem);
                                                loginViewModel.startUpdateMessagingToken();
                                                loginViewModel.setIsLogged(true);
                                            }else {
                                                loginViewModel.setIsLogged(true);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }


}
