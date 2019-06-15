package online.lahloba.www.lahloba.ui.favorites;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.FragmentFavoritesBinding;
import online.lahloba.www.lahloba.ui.adapters.FavoritesAdapter;
import online.lahloba.www.lahloba.utils.Injector;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel mViewModel;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }
    RecyclerView favoritesRV;
    FavoritesAdapter favoritesAdapter;
    List<ProductItem> productItems;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentFavoritesBinding binding = FragmentFavoritesBinding.inflate(inflater,container,false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext(),null);
        mViewModel = ViewModelProviders.of(this,factory).get(FavoritesViewModel.class);


        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            favoritesRV = binding.favoriteRV;
            favoritesAdapter = new FavoritesAdapter();
            favoritesRV.setHasFixedSize(true);

            productItems = new ArrayList<>();
            favoritesAdapter.setProductItems(productItems);

            staggeredGridLayoutManager =  new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            favoritesRV.setLayoutManager(staggeredGridLayoutManager);
            favoritesRV.setAdapter(favoritesAdapter);

            mViewModel.startGetFavoriteItems();

            mViewModel.getFavoritesItems().observe(this, favorites->{
                if (favorites==null) return;

                productItems.clear();
                favoritesAdapter.notifyDataSetChanged();

                productItems.addAll(favorites);
                favoritesAdapter.notifyDataSetChanged();

                Toast.makeText(getContext(), ""+favoritesAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
            });

        }


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
