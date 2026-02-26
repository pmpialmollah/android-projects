package com.nsoft.mybakery;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.nsoft.mybakery.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // my code ---------------------

        // --------- initialize here ---------------------------------------------------------------
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });
        // my code here ----------------------------------------------------------------------------

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

        binding.bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = new HomeFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

            return true;
        });

    }   // on create end here ----------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}   // main class end here -------------------------------------------------------------------------