package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.databinding.FragmentAccountBinding;
import online.lahloba.www.lahloba.ui.adapters.AccountAdapter;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.ExpandableHeightGridView;
import online.lahloba.www.lahloba.utils.Injector;

public class AccountFragment extends Fragment {
    private LoginViewModel loginViewModel;

    ExpandableHeightGridView accountGridView;
    List<MainMenuItem> mainMenuItems;
    AccountAdapter accountAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_account,
                container,
                false);

        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this.getContext(),null);
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);



        mainMenuItems = new ArrayList<>();



        loginViewModel.getCurrentUserDetails().observe(this, userItem -> {
            mainMenuItems.clear();
            accountAdapter.notifyDataSetChanged();


            if (userItem.isSeller()){
                addDefaultItems();

                MainMenuItem menuItem = new MainMenuItem();
                Uri uri = Uri.parse("android.resource://online.lahloba.www.lahloba/drawable/seller_order");
                menuItem.setImage(uri.toString());
                menuItem.setTitle(getResources().getString(R.string.seller));

                mainMenuItems.add(menuItem);

            }else {
                addDefaultItems();
            }







        });


        accountAdapter = new AccountAdapter(getContext(), loginViewModel);
        accountAdapter.setAccountItemList(mainMenuItems);
        accountAdapter.notifyDataSetChanged();
        accountGridView = binding.gridview;
        accountGridView.setExpanded(true);
        accountGridView.setAdapter(accountAdapter);


        View view = binding.getRoot();
        return view;
    }



    public void addMenuItem(String imageUri, String title){
        MainMenuItem menuItem = new MainMenuItem();
        Uri uri = Uri.parse(imageUri);
        menuItem.setImage(uri.toString());
        menuItem.setTitle(title);

        mainMenuItems.add(menuItem);
        if (accountAdapter != null){
            accountAdapter.notifyDataSetChanged();
        }

    }


    private void addDefaultItems(){
        addMenuItem(
                "android.resource://online.lahloba.www.lahloba/drawable/favorite",
                getResources().getString(R.string.my_favorite)
        );

        addMenuItem(
                "android.resource://online.lahloba.www.lahloba/drawable/order",
                getResources().getString(R.string.my_Orders)
        );

        addMenuItem(
                "android.resource://online.lahloba.www.lahloba/drawable/address",
                getResources().getString(R.string.my_adresses)
        );


        addMenuItem(
                "android.resource://online.lahloba.www.lahloba/drawable/signout",
                getResources().getString(R.string.sign_out)
        );
    }


}
