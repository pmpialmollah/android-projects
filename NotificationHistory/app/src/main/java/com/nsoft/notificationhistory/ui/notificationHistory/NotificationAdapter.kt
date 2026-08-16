package com.nsoft.notificationhistory.ui.notificationHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nsoft.notificationhistory.databinding.ItemNotificationBinding
import com.nsoft.notificationhistory.domain.model.AppNotification
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val onDeleteClick: (AppNotification) -> Unit
) : ListAdapter<AppNotification, NotificationAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = getItem(position)
        with(holder.binding) {
            tvAppName.text = notification.appName
            tvTitle.text = notification.title
            tvText.text = notification.text
            tvTime.text = formatTime(notification.postTime)
            
            try {
                val icon = root.context.packageManager.getApplicationIcon(notification.packageName)
                ivAppIcon.setImageDrawable(icon)
            } catch (e: Exception) {
                ivAppIcon.setImageDrawable(null)
            }

            root.setOnLongClickListener {
                onDeleteClick(notification)
                true
            }
        }
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AppNotification>() {
        override fun areItemsTheSame(oldItem: AppNotification, newItem: AppNotification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AppNotification, newItem: AppNotification): Boolean {
            return oldItem == newItem
        }
    }
}