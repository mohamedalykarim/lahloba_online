package online.lahloba.www.lahloba.ui.seller;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.SellerProductsFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.SellerProductAdapter;
import online.lahloba.www.lahloba.utils.Injector;


public class SellerProductsFragment extends Fragment {
    private SellerProductsViewModel mViewModel;
    String category;

    RecyclerView recyclerView;
    SellerProductAdapter adapter;
    List<ProductItem> productItemList;

    public static SellerProductsFragment newInstance() {
        return new SellerProductsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());

        mViewModel = ViewModelProviders.of(this, factory).get(SellerProductsViewModel.class);

        SellerProductsFragmentBinding binding = SellerProductsFragmentBinding.inflate(inflater,container,false);


        productItemList = new ArrayList<>();
        adapter = new SellerProductAdapter();
        adapter.setProductItems(productItemList);
        adapter.setViewModel(mViewModel);

        recyclerView = binding.recyclerView;

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mViewModel.startGetProductForCategoryAndUser(category);

        mViewModel.getProducts().observe(this, productItems -> {
            if (null == productItems) return;

            productItemList.clear();
            adapter.notifyDataSetChanged();

            productItemList.addAll(productItems);
            adapter.notifyDataSetChanged();


        });




        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SellerAddProductActivity.class));
            }
        });

        return binding.getRoot();
    }


    public void setCategory(String category) {
        this.category = category;
    }
}
