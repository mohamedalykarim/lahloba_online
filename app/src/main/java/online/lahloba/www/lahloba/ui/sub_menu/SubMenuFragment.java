package online.lahloba.www.lahloba.ui.sub_menu;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
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

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.data.model.SubMenuItem;
import online.lahloba.www.lahloba.databinding.FragmentSubMenuBinding;
import online.lahloba.www.lahloba.ui.adapters.SubMenuAdapter;
import online.lahloba.www.lahloba.utils.Injector;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;

public class SubMenuFragment extends Fragment {
    String subMenuID;
    List<SubMenuItem> items;
    SubMenuViewModel viewModel;
    SubMenuAdapter subMenuAdapter;
    FragmentSubMenuBinding binding;

    public static SubMenuFragment newInstance(Bundle args) {
        SubMenuFragment fragment = new SubMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sub_menu, container,false);
        subMenuID = getArguments().getString(EXTRA_SUBTITLE_ID);
        items = new ArrayList<>();

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.SubMenuRecycler);

        ViewModelProviderFactory factory = Injector.getVMFactory(this.getContext());
        viewModel = ViewModelProviders.of(this, factory).get(SubMenuViewModel.class);
        binding.setSubMenuViewModel(viewModel);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        subMenuAdapter = new SubMenuAdapter(getContext());
        subMenuAdapter.setSubMenuItems(items);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(subMenuAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        viewModel.startSupMenuItems(subMenuID);

        viewModel.getSupMenuItems().observe(this, itemList->{
            items.clear();
            subMenuAdapter.notifyDataSetChanged();


            items.addAll(itemList);
            subMenuAdapter.notifyDataSetChanged();
        });

    }


}
