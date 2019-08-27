package online.lahloba.www.lahloba.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import online.lahloba.www.lahloba.data.model.CityItem;
import online.lahloba.www.lahloba.databinding.CityItemBinding;
import online.lahloba.www.lahloba.databinding.CountryItemBinding;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;

public class CityAdapter extends
        ExpandableRecyclerViewAdapter<CityAdapter.CountryViewHolder, CityAdapter.CityViewHolder> {
    OnChildClicked onChildClicked;


    public CityAdapter(List<? extends ExpandableGroup> groups, OnChildClicked onChildClicked) {
        super(groups);
        this.onChildClicked = onChildClicked;
    }


    @Override
    public CountryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        CountryItemBinding binding = CountryItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CountryViewHolder(binding);
    }

    @Override
    public CityViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        CityItemBinding binding = CityItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CityViewHolder(binding);
    }

    @Override
    public void onBindChildViewHolder(CityViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        CityItem cityItem = (CityItem) group.getItems().get(childIndex);

        holder.binding.textView30.setText(cityItem.getName());


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.setCurrentLocationLat(holder.binding.getRoot().getContext(), String.valueOf(cityItem.getLat()));
                SharedPreferencesManager.setCurrentLocationLan(holder.binding.getRoot().getContext(), String.valueOf(cityItem.getLan()));
                SharedPreferencesManager.setCurrentLocationAddress(holder.binding.getRoot().getContext(), cityItem.getName());

                Toast.makeText(holder.binding.getRoot().getContext(),
                        cityItem.getName() + " "+ "City has choosen", Toast.LENGTH_SHORT).show();

                onChildClicked.onChildClicked(cityItem.getName());

            }
        });
    }

    @Override
    public void onBindGroupViewHolder(CountryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.binding.textView29.setText(group.getTitle());

    }

    class CountryViewHolder extends GroupViewHolder{
        CountryItemBinding binding;
        public CountryViewHolder(CountryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class CityViewHolder extends ChildViewHolder {
        CityItemBinding binding;
        public CityViewHolder(CityItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface OnChildClicked{
        void onChildClicked(String currentLocation);
    }


}
