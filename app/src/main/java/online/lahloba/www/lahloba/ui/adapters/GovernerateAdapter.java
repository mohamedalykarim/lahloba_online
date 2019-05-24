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

import online.lahloba.www.lahloba.data.model.Governerate;
import online.lahloba.www.lahloba.databinding.CountryItemBinding;
import online.lahloba.www.lahloba.databinding.GovernerateItemBinding;
import online.lahloba.www.lahloba.utils.SharedPreferencesManager;

public class GovernerateAdapter extends
        ExpandableRecyclerViewAdapter<GovernerateAdapter.CountryViewHolder, GovernerateAdapter.GovernerateViewHolder> {
    OnChildClicked onChildClicked;


    public GovernerateAdapter(List<? extends ExpandableGroup> groups, OnChildClicked onChildClicked) {
        super(groups);
        this.onChildClicked = onChildClicked;
    }


    @Override
    public CountryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        CountryItemBinding binding = CountryItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CountryViewHolder(binding);
    }

    @Override
    public GovernerateViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        GovernerateItemBinding binding = GovernerateItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new GovernerateViewHolder(binding);
    }

    @Override
    public void onBindChildViewHolder(GovernerateViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Governerate governerate = (Governerate) group.getItems().get(childIndex);

        holder.binding.textView30.setText(governerate.getName());


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferencesManager.setCurrentLocationLat(holder.binding.getRoot().getContext(), String.valueOf(governerate.getLat()));
                SharedPreferencesManager.setCurrentLocationLan(holder.binding.getRoot().getContext(), String.valueOf(governerate.getLan()));
                SharedPreferencesManager.setCurrentLocationAddress(holder.binding.getRoot().getContext(), governerate.getName());

                Toast.makeText(holder.binding.getRoot().getContext(),
                        governerate.getName() + " "+ "governorate has choosen", Toast.LENGTH_SHORT).show();

                onChildClicked.onChildClicked(governerate.getName());

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

    class GovernerateViewHolder extends ChildViewHolder {
        GovernerateItemBinding binding;
        public GovernerateViewHolder(GovernerateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }


    public interface OnChildClicked{
        void onChildClicked(String currentLocation);
    }


}
