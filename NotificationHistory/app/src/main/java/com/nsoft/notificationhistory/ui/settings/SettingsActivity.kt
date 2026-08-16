package com.nsoft.notificationhistory.ui.settings

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nsoft.notificationhistory.MyApplication
import com.nsoft.notificationhistory.data.receiver.NotificationRebindReceiver
import com.nsoft.notificationhistory.data.util.DeviceUtils
import com.nsoft.notificationhistory.data.util.PowerSettingsHelper
import com.nsoft.notificationhistory.databinding.ActivitySettingsBinding
import com.nsoft.notificationhistory.ui.ViewModelFactory
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels {
        ViewModelFactory((application as MyApplication).appContainer)
    }
    private lateinit var adapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.scrollView.updatePadding(bottom = systemBars.bottom)
            insets
        }

        setupToolbar()
        setupHealthSection()
        setupRecyclerView()
        observeState()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupHealthSection() {
        if (DeviceUtils.isMiui()) {
            binding.btnAutostart.visibility = View.VISIBLE
            binding.btnAutostart.setOnClickListener {
                PowerSettingsHelper.openAutoStartSettings(this)
            }
        }

        binding.btnBattery.setOnClickListener {
            PowerSettingsHelper.requestIgnoreBatteryOptimization(this)
        }

        binding.btnRebind.setOnClickListener {
            NotificationRebindReceiver.rebindNotificationListener(this)
        }
    }

    private fun setupRecyclerView() {
        adapter = AppAdapter { packageName, isEnabled ->
            viewModel.toggleAppSetting(packageName, isEnabled)
        }
        binding.rvApps.adapter = adapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apps.collect { apps ->
                        adapter.submitList(apps)
                    }
                }
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }
}
