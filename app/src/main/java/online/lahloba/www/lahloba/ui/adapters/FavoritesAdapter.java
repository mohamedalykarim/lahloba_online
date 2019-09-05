package online.lahloba.www.lahloba.ui.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.FavoriteItem;
import online.lahloba.www.lahloba.data.model.ProductItem;
import online.lahloba.www.lahloba.databinding.RowFavoriteItemBinding;
import online.lahloba.www.lahloba.ui.favorites.FavoritesViewModel;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    List<FavoriteItem> favoriteItems;
    private FavoritesViewModel mViewModel;
    Context mContext;

    public FavoritesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RowFavoriteItemBinding binding = RowFavoriteItemBinding.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                viewGroup,
                false
        );



        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        mViewModel.startGetProductById(favoriteItems.get(position).getProductId());



        mViewModel.getProductItem().observe((LifecycleOwner) mContext, productItem -> {
            if (productItem == null)return;
            if (favoriteItems == null)return;
            if (favoriteItems.size() == 0) return;



            if (productItem.getId().equals(favoriteItems.get(position).getProductId())){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                storageRef.child(productItem.getImage())
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){

                                    Picasso.get().load(task.getResult())
                                            .placeholder(R.drawable.progress_animation)
                                            .into(holder.binding.image);


                                }
                            }
                        });



                holder.binding.setProduct(productItem);
            }


        });




    }

    @Override
    public int getItemCount() {
        return favoriteItems==null ? 0:favoriteItems.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final RowFavoriteItemBinding binding;

        public FavoriteViewHolder(RowFavoriteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setFavoriteItems(List<FavoriteItem> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

    public void setmViewModel(FavoritesViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }
}
