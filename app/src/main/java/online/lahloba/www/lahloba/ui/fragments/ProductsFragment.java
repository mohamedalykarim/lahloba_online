package online.lahloba.www.lahloba.ui.fragments;

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
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.ui.adapters.ProductAdapter;
import online.lahloba.www.lahloba.ui.products.ProductsViewModel;

public class ProductsFragment extends Fragment {

    private ProductsViewModel mViewModel;

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        List<ProductItem> productItemList = new ArrayList<>();
        RecyclerView productsRV = view.findViewById(R.id.productsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.setProductItemList(productItemList);
        productsRV.setLayoutManager(linearLayoutManager);
        productsRV.setAdapter(productAdapter);

        for (int i = 0; i<20; i++){
            ProductItem productItem = new ProductItem();
            productItem.setId("productid"+i);
            productItem.setTitle("Product Name Here"+i);
            productItem.setPrice("150");
            productItem.setCurrency("EGP");
            productItemList.add(productItem);
        }

        productAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        // TODO: Use the ViewModel
    }

}
