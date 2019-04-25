package online.lahloba.www.lahloba.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.data.model.VMPHelper;
import online.lahloba.www.lahloba.ui.adapters.ProductAdapter;
import online.lahloba.www.lahloba.ui.products.ProductsViewModel;
import online.lahloba.www.lahloba.utils.Constants;
import online.lahloba.www.lahloba.utils.Injector;

public class ProductsFragment extends Fragment {

    private ProductsViewModel mViewModel;

    public static ProductsFragment newInstance(Bundle args) {
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(args);
        return fragment;
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

        Bundle bundle = getArguments();
        String subId = bundle.getString(Constants.EXTRA_SUBTITLE_ID);

        VMPHelper vmpHelper = new VMPHelper();
        vmpHelper.setCategoryId(subId);

        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext(), vmpHelper);
        mViewModel = ViewModelProviders.of(this, factory).get(ProductsViewModel.class);


        mViewModel.startProductsForCategory();

        mViewModel.getProductsForCategory().observe(this, products->{
            productItemList.clear();
            productItemList.addAll(products);
            productAdapter.notifyDataSetChanged();

        });



        return view;
    }


}
