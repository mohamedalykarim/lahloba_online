package online.lahloba.www.lahloba.ui.seller;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.SellerProductsFragmentBinding;
import online.lahloba.www.lahloba.ui.adapters.SellerProductAdapter;
import online.lahloba.www.lahloba.utils.Constants;
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






        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SellerAddProductActivity.class);
                intent.putExtra(Constants.EXTRA_SUBTITLE_ID, category);

                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.getProducts().observe(this, productItems -> {
            if (null == productItems) return;

            productItemList.clear();
            adapter.notifyDataSetChanged();

            productItemList.addAll(productItems);
            adapter.notifyDataSetChanged();

        });
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
