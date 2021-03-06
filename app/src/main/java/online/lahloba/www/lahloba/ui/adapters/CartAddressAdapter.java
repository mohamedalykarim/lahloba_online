package online.lahloba.www.lahloba.ui.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.RowCartAddressBinding;

public class CartAddressAdapter extends RecyclerView.Adapter<CartAddressAdapter.CartAddressViewHolder> {

    OnAddressSelected onAddressSelected;

    private List<AddressItem> addressItems;

    public CartAddressAdapter(OnAddressSelected onAddressSelected) {
        this.onAddressSelected = onAddressSelected;
    }

    @NonNull
    @Override
    public CartAddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RowCartAddressBinding binding = RowCartAddressBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false);



        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressSelected.onAddressItemSelected(addressItems.get(i));
            }
        });

        return new CartAddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAddressViewHolder holder, int position) {
        holder.binding.setAddress(addressItems.get(position));

    }

    @Override
    public int getItemCount() {
        return null == addressItems? 0 : addressItems.size() ;
    }

    class CartAddressViewHolder extends RecyclerView.ViewHolder {
        RowCartAddressBinding binding;
        public CartAddressViewHolder(RowCartAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public void setAddressItems(List<AddressItem> addressItems) {
        this.addressItems = addressItems;
    }

    public interface OnAddressSelected{
        void onAddressItemSelected(AddressItem addressItems);
    }
}
