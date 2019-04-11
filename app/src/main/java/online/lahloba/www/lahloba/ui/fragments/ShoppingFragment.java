package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.MainMenuItem;
import online.lahloba.www.lahloba.ui.adapters.ShoppingAdapter;
import online.lahloba.www.lahloba.ui.main.MainViewModel;
import online.lahloba.www.lahloba.utils.Injector;

public class ShoppingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext(), null);
        MainViewModel mainViewModel = ViewModelProviders.of(this.getActivity(),factory).get(MainViewModel.class);


        GridView gridView = view.findViewById(R.id.gridview);
        ShoppingAdapter shoppingAdapter = new ShoppingAdapter();


        gridView.setAdapter(shoppingAdapter);

        mainViewModel.getMainMenuItems().observe(this.getActivity(),items->{
            shoppingAdapter.setShoppingItemList(items);
            shoppingAdapter.notifyDataSetChanged();
        });





        super.onViewCreated(view, savedInstanceState);
    }
}
