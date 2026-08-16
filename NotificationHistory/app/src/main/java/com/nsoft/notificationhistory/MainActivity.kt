package com.nsoft.notificationhistory

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.nsoft.notificationhistory.data.listener.NotificationListener
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup.MarginLayoutParams
import com.nsoft.notificationhistory.databinding.ActivityMainBinding
import com.nsoft.notificationhistory.ui.ViewModelFactory
import com.nsoft.notificationhistory.ui.notificationHistory.NotificationAdapter
import com.nsoft.notificationhistory.ui.notificationHistory.NotificationHistoryViewModel
import com.nsoft.notificationhistory.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NotificationHistoryViewModel by viewModels {
        ViewModelFactory((application as MyApplication).appContainer)
    }
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.rvNotifications.updatePadding(bottom = systemBars.bottom)
            binding.fabSettings.updateLayoutParams<MarginLayoutParams> {
                val baseMargin = resources.getDimensionPixelSize(R.dimen.margin_normal)
                bottomMargin = baseMargin + systemBars.bottom
            }
            insets
        }

        checkNotificationAccess()
        checkBatteryOptimization()
        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        setupListeners()
        observeState()
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_clear_all -> {
                    AlertDialog.Builder(this).
                        setTitle("Confirm clear!")
                        .setMessage("Do you really want to clear all notifications?")
                        .setPositiveButton("Yes"){dialog, _ ->
                            viewModel.clearAll()
                        }
                        .setNegativeButton("No") {dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()

                    true
                }
                R.id.action_reconnect -> {
                    tryRebindService()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkNotificationAccess()
    }



    private fun setupRecyclerView() {
        adapter = NotificationAdapter { notification ->
            viewModel.deleteNotification(notification)
        }
        binding.rvNotifications.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.rvNotifications.scrollToPosition(0)
                }
            }
        })
    }

    private fun setupSearchView() {
        binding.searchView.editText.addTextChangedListener {
            viewModel.onSearchQueryChanged(it.toString())
        }
    }

    private fun setupListeners() {
        binding.fabSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredNotifications.collect { notifications ->
                    adapter.submitList(notifications)
                    binding.tvEmpty.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun checkNotificationAccess() {
        if (!isNotificationServiceEnabled()) {
            com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Notification Access Required")
                .setMessage("This app needs notification access to capture and store your notification history. Please enable it in the settings.")
                .setPositiveButton("Settings") { _, _ ->
                    startActivity(Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat?.contains(packageName) == true
    }

    private fun checkBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                    .setTitle("Battery Optimization")
                    .setMessage("To ensure the app can capture notifications in the background, please disable battery optimization for this app.")
                    .setPositiveButton("Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    private fun tryRebindService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val componentName = ComponentName(this, NotificationListener::class.java)
            NotificationListenerService.requestRebind(componentName)
            Snackbar.make(binding.root, "Attempting to reconnect service...", Snackbar.LENGTH_SHORT).show()
        }
    }
}
