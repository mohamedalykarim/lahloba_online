package online.lahloba.www.lahloba.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.RowAddressItemBinding;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    List<AddressItem> addressItemList;


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RowAddressItemBinding binding = RowAddressItemBinding.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                viewGroup,
                false
                );

        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int i) {
        holder.binding.flatFloorTv.setText("Flat Number "+addressItemList.get(i).getFlatNumber()
                + " - "+addressItemList.get(i).getFloor() +"th Floor");

        holder.binding.buildingStreetTv.setText(addressItemList.get(i).getBuilding() + " - "
                + addressItemList.get(i).getStreet());

        holder.binding.zoneCityCountryTv.setText(
                addressItemList.get(i).getZone()
                        + " - " +
                addressItemList.get(i).getCity()
                        + " - " +
                addressItemList.get(i).getCountry()
        );
    }

    @Override
    public int getItemCount() {
        return null == addressItemList? 0 : addressItemList.size() ;
    }

    class AddressViewHolder extends RecyclerView.ViewHolder{
        RowAddressItemBinding binding;

        public AddressViewHolder(RowAddressItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setAddressItemList(List<AddressItem> addressItemList) {
        this.addressItemList = addressItemList;
    }
}
