package com.nsoft.sqliteapp.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.nsoft.sqliteapp.R;
import com.nsoft.sqliteapp.databinding.ActivityMainBinding;
import com.nsoft.sqliteapp.ui.fragments.AddFragment;
import com.nsoft.sqliteapp.ui.fragments.AnalyticsFragment;
import com.nsoft.sqliteapp.ui.fragments.HomeFragment;
import com.nsoft.sqliteapp.ui.fragments.SettingsFragment;
import com.nsoft.sqliteapp.ui.fragments.ShowFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // -----------------------------------------------------------------------------------------

        startFragment(new HomeFragment());

        binding.bnDashboard.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.navHome) {

                startFragment(new HomeFragment());
                binding.getRoot().setBackgroundResource(R.drawable.topbar_background);

            } else if (id == R.id.navList) {

                startFragment(new ShowFragment());
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_gray));

            } else if (id == R.id.navAdd) {

                startFragment(new AddFragment());
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_gray));

            } else if (id == R.id.navAnalytics) {

                startFragment(new AnalyticsFragment());
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_gray));

            } else {

                startFragment(new SettingsFragment());
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.light_gray));

            }
            return true;
        });


        // -----------------------------------------------------------------------------------------
    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.flDashboard, fragment).commit();
    }
}