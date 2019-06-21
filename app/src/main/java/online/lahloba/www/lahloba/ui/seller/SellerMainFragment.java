package online.lahloba.www.lahloba.ui.seller;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.SellerMainMenuItem;
import online.lahloba.www.lahloba.databinding.SellerMainFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.SellerMainMenuAdapter;
import online.lahloba.www.lahloba.utils.Injector;


public class SellerMainFragment extends Fragment {
    private SellerMainViewModel mViewModel;

    RecyclerView menuRv;
    SellerMainMenuAdapter adapter;
    List<SellerMainMenuItem> menuItems;

    public static SellerMainFragment newInstance() {
        return new SellerMainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SellerMainFragmentBinding binding = SellerMainFragmentBinding.inflate(inflater,container,false);

        menuItems = new ArrayList<>();

        adapter = new SellerMainMenuAdapter();
        adapter.setMenuItemList(menuItems);


        menuRv = binding.menuRv;
        menuRv.setAdapter(adapter);
        menuRv.setLayoutManager(new LinearLayoutManager(getContext()));
        menuRv.setHasFixedSize(true);


        addMenuItems();


        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SellerMainViewModel.class);

        return binding.getRoot();
    }


    /**
     * Add items of menu
     */

    private void addMenuItems() {
        addMenuItem("Orders",
                ".ui.seller.SellerOrdersActivity",
                R.drawable.seller_orders_icon);

        addMenuItem("Add product",
                ".ui.seller.SellerAddProductActivity",
                R.drawable.seller_add_poduct_icon );

        addMenuItem("Edit product",
                ".ui.seller.SellerEditProductActivity",
                R.drawable.seller_edit_poduct_icon );

    }


    /**
     * Add item to seller menu
     * @param title of menu
     * @param activity that will start when click
     * @param image image of item
     */
    private void addMenuItem(String title, String activity, int image) {
        SellerMainMenuItem item = new SellerMainMenuItem();
        item.setTitle(title);
        item.setImg(image);
        item.setUri(activity);

        menuItems.add(item);
        adapter.notifyDataSetChanged();

    }


}
