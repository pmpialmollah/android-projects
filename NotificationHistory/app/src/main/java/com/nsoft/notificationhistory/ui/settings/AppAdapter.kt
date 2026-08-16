package com.nsoft.notificationhistory.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.notificationhistory.databinding.ItemAppBinding
import com.nsoft.notificationhistory.domain.model.AppInfo

class AppAdapter(
    private val onToggle: (String, Boolean) -> Unit
) : ListAdapter<AppInfo, AppAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding, onToggle)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = getItem(position)
        holder.bind(app)
    }

    class AppViewHolder(
        private val binding: ItemAppBinding,
        private val onToggle: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppInfo) {
            binding.ivAppIcon.setImageDrawable(app.icon)
            binding.tvAppName.text = app.appName
            
            binding.swNotification.setOnCheckedChangeListener(null)
            binding.swNotification.isChecked = app.isEnabled
            binding.swNotification.setOnCheckedChangeListener { _, isChecked ->
                onToggle(app.packageName, isChecked)
            }
        }
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName && 
                   oldItem.appName == newItem.appName && 
                   oldItem.isEnabled == newItem.isEnabled
        }
    }
}
