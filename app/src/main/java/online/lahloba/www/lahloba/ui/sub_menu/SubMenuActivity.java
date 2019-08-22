package online.lahloba.www.lahloba.ui.sub_menu;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.databinding.ActivitySubMenuBinding;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;

public class SubMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySubMenuBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_sub_menu);
        binding.setLifecycleOwner(this);

        Intent intent = getIntent();
        if(null != intent && intent.hasExtra(EXTRA_SUBTITLE_ID)){
            String id = intent.getStringExtra(EXTRA_SUBTITLE_ID);

            Bundle args = new Bundle();
            args.putString(EXTRA_SUBTITLE_ID, id);

            // load fragment
            Fragment fragment = SubMenuFragment.newInstance(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_container, fragment);
            transaction.commit();
        }
    }
}
