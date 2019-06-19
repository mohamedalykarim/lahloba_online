package online.lahloba.www.lahloba.ui.news;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ViewModelProviderFactory;
import online.lahloba.www.lahloba.utils.Injector;

public class NewsFragment extends Fragment {

    private NewsViewModel mViewModel;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewModelProviderFactory factory = Injector.getVMFactory(getContext(),null);
        mViewModel = ViewModelProviders.of(this, factory).get(NewsViewModel.class);

        return inflater.inflate(R.layout.fragment_news, container, false);
    }


}
