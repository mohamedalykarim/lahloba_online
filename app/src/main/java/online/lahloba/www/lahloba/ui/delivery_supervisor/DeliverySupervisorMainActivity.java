package online.lahloba.www.lahloba.ui.delivery_supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import online.lahloba.www.lahloba.R;


public class DeliverySupervisorMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_supervisor_main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DeliverySupervisorMainFragment.newInstance())
                    .commitNow();
        }
    }
}
