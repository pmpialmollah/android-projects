package com.nsoft.mybakery

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.nsoft.mybakery.databinding.ActivityMainBinding
import com.nsoft.mybakery.fragments.AnalyticsFragment
import com.nsoft.mybakery.fragments.CustomerFragment
import com.nsoft.mybakery.fragments.HomeFragment
import com.nsoft.mybakery.fragments.ProductsFragment
import com.nsoft.mybakery.fragments.SellFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        // my code starts here ---------------------------------------------------------------------

        // Set initial fragment -----------
        loadFragment(HomeFragment())

        // Inflate and add custom bottom nav -----------
        val bottomNavView =
            layoutInflater.inflate(R.layout.bottom_nav_layout, binding.bottomNavContainer, false)
        binding.bottomNavContainer.addView(bottomNavView)

        // bottom nav items -------------------------------
        val navHome = bottomNavView.findViewById<ImageView>(R.id.navHome)
        val navProducts = bottomNavView.findViewById<ImageView>(R.id.navProducts)
        val navSell = bottomNavView.findViewById<CardView>(R.id.navSell)
        val navCustomers = bottomNavView.findViewById<ImageView>(R.id.navCustomers)
        val navAnalytics = bottomNavView.findViewById<ImageView>(R.id.navAnalytics)

        // click listeners -------------------------------------
        navHome.setOnClickListener { loadFragment(HomeFragment()) }
        navProducts.setOnClickListener { loadFragment(ProductsFragment()) }
        navSell.setOnClickListener { loadFragment(SellFragment()) }
        navCustomers.setOnClickListener { loadFragment(CustomerFragment()) }
        navAnalytics.setOnClickListener { loadFragment(AnalyticsFragment()) }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
