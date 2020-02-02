package com.natasha.clockio.base.ui

import com.natasha.clockio.R
import com.natasha.clockio.base.constant.UserConst

fun setStatusIcon(statusText: String?): Int {
    var iconPosition: Int = R.drawable.ic_status_online_24dp
    when(statusText) {
        UserConst.STATUS_ONLINE -> iconPosition = R.drawable.ic_status_online_24dp
        UserConst.STATUS_MEETING -> iconPosition = R.drawable.ic_status_meeting_24dp
        UserConst.STATUS_AWAY -> iconPosition = R.drawable.ic_status_away_24dp
        UserConst.STATUS_OFFLINE -> iconPosition = R.drawable.ic_status_offline_24dp
    }
    return iconPosition
}