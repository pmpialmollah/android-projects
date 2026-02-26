package com.nsoft.mybakery;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nsoft.mybakery.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());             // my code
        setContentView(binding.getRoot());                                      // my code
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });
        // my code here ----------------------------------------------------------------------------

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

        binding.bottomNav.setOnItemSelectedListener(menuItem -> {

            int menuId = menuItem.getItemId();

            if (menuId == R.id.menuHome) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
            } else if (menuId == R.id.menuProducts) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProductsFragment()).commit();
            } else if (menuId == R.id.menuCustomers) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CustomerFragment()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ToolsFragment()).commit();
            }

            return true;
        });


    }   // on create end here ----------------------------------------------------------------------


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}   // main class end here -------------------------------------------------------------------------