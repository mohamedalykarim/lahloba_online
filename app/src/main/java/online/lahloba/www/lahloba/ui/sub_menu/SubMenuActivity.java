package online.lahloba.www.lahloba.ui.sub_menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import online.lahloba.www.lahloba.R;
import online.lahloba.www.lahloba.utils.Constants;

public class SubMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);

        Intent intent = getIntent();
        if(null != intent && intent.hasExtra(Constants.EXTRA_SUBTITLE_ID)){
            String id = intent.getStringExtra(Constants.EXTRA_SUBTITLE_ID);
            Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        }
    }
}
