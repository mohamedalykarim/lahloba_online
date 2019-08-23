package online.lahloba.www.lahloba.ui.fragments;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.databinding.FragmentAccountBinding;
import online.lahloba.www.lahloba.ui.adapters.AccountAdapter;
import online.lahloba.www.lahloba.ui.login.LoginViewModel;
import online.lahloba.www.lahloba.utils.Injector;

public class AccountFragment extends Fragment {
    private LoginViewModel loginViewModel;

    GridView gridView;
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

        ViewModelProviderFactory loginFactory = Injector.getVMFactory(this.getContext());
        loginViewModel = ViewModelProviders.of(this,loginFactory).get(LoginViewModel.class);


        gridView = binding.gridview;

        mainMenuItems = new ArrayList<>();


        accountAdapter = new AccountAdapter(getContext(), loginViewModel);
        accountAdapter.setAccountItemList(mainMenuItems);
        accountAdapter.notifyDataSetChanged();


        gridView.setAdapter(accountAdapter);


        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loginViewModel.getCurrentUserDetails().observe(this, userItem -> {
            if (userItem == null){
                return;
            }

            mainMenuItems.clear();


            if (userItem.isSeller()){
                MainMenuItem ordersItem = new MainMenuItem();
                Uri ordersUri = Uri.parse("android.resource://online.lahloba.www.lahloba/drawable/seller_order");
                ordersItem.setImage(ordersUri.toString());
                ordersItem.setTitle(getResources().getString(R.string.seller_orders));

                mainMenuItems.add(ordersItem);

                MainMenuItem productItem = new MainMenuItem();
                Uri productsUri = Uri.parse("android.resource://online.lahloba.www.lahloba/drawable/seller_products");
                productItem.setImage(productsUri.toString());
                productItem.setTitle(getResources().getString(R.string.seller_Products));

                mainMenuItems.add(productItem);

                mainMenuItems.addAll(getDefaultItems());
                accountAdapter.notifyDataSetChanged();

            }else{
                mainMenuItems.addAll(getDefaultItems());
                accountAdapter.notifyDataSetChanged();

            }


        });

    }

    public MainMenuItem getMenuItem(String imageUri, String title){
        MainMenuItem menuItem = new MainMenuItem();
        Uri uri = Uri.parse(imageUri);
        menuItem.setImage(uri.toString());
        menuItem.setTitle(title);

        return menuItem;
    }


    private List<MainMenuItem> getDefaultItems(){
        List<MainMenuItem> defaultItems = new ArrayList<>();


        defaultItems.add(
                getMenuItem(
                        "android.resource://online.lahloba.www.lahloba/drawable/cart",
                        getResources().getString(R.string.cart)
                )
        );

        defaultItems.add(
                getMenuItem(
                        "android.resource://online.lahloba.www.lahloba/drawable/favorite",
                        getResources().getString(R.string.my_favorite)
                )
        );

        defaultItems.add(
                getMenuItem(
                        "android.resource://online.lahloba.www.lahloba/drawable/order",
                        getResources().getString(R.string.my_Orders)
                )
        );

        defaultItems.add(
                getMenuItem(
                        "android.resource://online.lahloba.www.lahloba/drawable/address",
                        getResources().getString(R.string.my_adresses)
                )
        );

        defaultItems.add(
                getMenuItem(
                        "android.resource://online.lahloba.www.lahloba/drawable/signout",
                        getResources().getString(R.string.sign_out)
                )
        );

        return defaultItems;
    }



}
