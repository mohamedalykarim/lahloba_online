package online.lahloba.www.lahloba.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.data.model.AddressItem;
import online.lahloba.www.lahloba.databinding.RowAddressItemBinding;
import online.lahloba.www.lahloba.ui.address.AddressViewModel;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    List<AddressItem> addressItemList;
    AddressViewModel addressViewModel;
    EditAddressClickListener editAddressClickListener;

    public AddressAdapter(AddressViewModel addressViewModel) {
        this.addressViewModel = addressViewModel;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            RowAddressItemBinding binding = RowAddressItemBinding.inflate(
                    LayoutInflater.from(viewGroup.getContext()),
                    viewGroup,
                    false);
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

            if (addressItemList.get(i).isDefaultAddress()){
                holder.binding.defaultBtn.setBackgroundResource(R.drawable.btn_bg_3);
            }else {
                holder.binding.defaultBtn.setBackgroundResource(R.drawable.btn_bg_2);
            }


        /**
         * Default Button
         */

        holder.binding.defaultBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addressViewModel.startSetDefaultAddress(addressItemList.get(i).getId());
                }
            });

        /**
         * Delete Button
         */

        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addressViewModel.startDeleteAddress(addressItemList.get(i).getId());
                }
            });


        /**
         * Edit Button
         */
        holder.binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddressClickListener.onEditAddressClicked(addressItemList.get(i));
            }
        });

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


    public void setEditAddressClickListener(EditAddressClickListener editAddressClickListener) {
        this.editAddressClickListener = editAddressClickListener;
    }

    public interface EditAddressClickListener{
        void onEditAddressClicked(AddressItem addressItem);
    }



}
