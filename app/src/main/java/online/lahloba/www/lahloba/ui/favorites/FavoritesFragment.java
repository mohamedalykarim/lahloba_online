package online.lahloba.www.lahloba.ui.favorites;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.FavoriteItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.FragmentFavoritesBinding;
import online.lahloba.www.lahloba.ui.adapters.FavoritesAdapter;
import online.lahloba.www.lahloba.utils.Injector;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel mViewModel;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }
    FavoritesAdapter favoritesAdapter;
    List<FavoriteItem> favoriteItems;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentFavoritesBinding binding = FragmentFavoritesBinding.inflate(inflater,container,false);

        ViewModelProviderFactory factory = Injector.getVMFactory(getContext());
        mViewModel = ViewModelProviders.of(this,factory).get(FavoritesViewModel.class);


        if (FirebaseAuth.getInstance().getCurrentUser()!=null){

            favoriteItems = new ArrayList<>();


            favoritesAdapter = new FavoritesAdapter(getContext());
            favoritesAdapter.setFavoriteItems(favoriteItems);
            favoritesAdapter.setmViewModel(mViewModel);


            staggeredGridLayoutManager =  new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

            binding.favoriteRV.setHasFixedSize(true);
            binding.favoriteRV.setLayoutManager(staggeredGridLayoutManager);
            binding.favoriteRV.setAdapter(favoritesAdapter);

            mViewModel.startGetFavoriteItems();



        }


        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        mViewModel.getFavoritesItems().observe(this, favorites->{
            if (favorites==null) return;

            favoriteItems.clear();
            favoritesAdapter.notifyDataSetChanged();

            favoriteItems.addAll(favorites);
            favoritesAdapter.notifyDataSetChanged();

            Toast.makeText(getContext(), ""+favoritesAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
        });
    }
}
