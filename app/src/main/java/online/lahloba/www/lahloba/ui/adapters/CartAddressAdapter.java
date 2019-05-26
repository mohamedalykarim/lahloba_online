package online.lahloba.www.lahloba.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.List;

import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.RowCartAddressBinding;
import online.lahloba.www.lahloba.utils.ExpandableHeightRecyclerView;

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

        binding.nameTV.setText(addressItems.get(i).getName());
        binding.addressTV.setText(
                addressItems.get(i).getCountry()+"-"
                        + addressItems.get(i).getCity()+"-"
                        + addressItems.get(i).getZone()+"-"
                        + addressItems.get(i).getStreet()+"-"
                        + addressItems.get(i).getBuilding()+"-"
                        + addressItems.get(i).getFloor()+"-"
                        + addressItems.get(i).getFlatNumber()
        );

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressSelected.onAddressItemSelected(addressItems.get(i));
            }
        });

        return new CartAddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAddressViewHolder cartAddressViewHolder, int i) {

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
