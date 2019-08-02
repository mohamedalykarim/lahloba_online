package online.lahloba.www.lahloba.ui.seller;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.databinding.SellerProductFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.SubMenuAdapter;
import online.lahloba.www.lahloba.ui.adapters.SubMenuChooseAdapter;
import online.lahloba.www.lahloba.utils.Injector;


public class SellerProductsFragment extends Fragment {
    private SellerProductsViewModel mViewModel;

    SubMenuChooseAdapter adapter;
    RecyclerView recyclerView;
    List<SubMenuItem> menuItemList;

    public static SellerProductsFragment newInstance() {
        return new SellerProductsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SellerProductFragmentBinding binding = SellerProductFragmentBinding.inflate(inflater,container,false);

        recyclerView = binding.menuRv;
        adapter = new SubMenuChooseAdapter(getContext());
        menuItemList = new ArrayList<>();

        adapter.setSubMenuItems(menuItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);





        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this, factory).get(SellerProductsViewModel.class);

        mViewModel.startGetSubMenusWithNoChild();

        mViewModel.getSubMenuItems().observe(this, subMenuItems -> {
            menuItemList.clear();

            menuItemList.addAll(subMenuItems);
            adapter.notifyDataSetChanged();
        });

        binding.categoryET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userInput = s.toString();
                List<SubMenuItem> newMenuItemList = new ArrayList<>();

                for (SubMenuItem item: menuItemList){
                    if (item.getTitle().contains(userInput)){
                        newMenuItemList.add(item);
                    }
                }

                adapter.setSubMenuItems(newMenuItemList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }







}
