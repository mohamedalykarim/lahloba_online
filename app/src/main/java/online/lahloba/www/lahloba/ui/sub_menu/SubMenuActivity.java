package online.lahloba.www.lahloba.ui.sub_menu;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.ui.fragments.SubMenuFragment;
import online.lahloba.www.lahloba.utils.Constants;

import static online.lahloba.www.lahloba.utils.Constants.EXTRA_SUBTITLE_ID;
import static online.lahloba.www.lahloba.utils.Constants.GET_SUB_MENU_ITEMS;

public class SubMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);

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
