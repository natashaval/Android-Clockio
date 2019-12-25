package com.natasha.clockio.notification.ui

import androidx.lifecycle.ViewModel
import com.natasha.clockio.notification.repository.NotifRepository
import javax.inject.Inject

class NotifViewModel @Inject constructor(private val notifRepository: NotifRepository) : ViewModel() {
}
