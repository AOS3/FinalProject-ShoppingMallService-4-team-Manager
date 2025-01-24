package com.aladin.shoppingmallservice_4_team_manager.ui.notificationManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNotificationSendBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationSendFragment : Fragment() {

    private var _fragmentNotificationSendBinding: FragmentNotificationSendBinding? = null
    private val fragmentNotificationSendBinding get() = _fragmentNotificationSendBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNotificationSendBinding =
            FragmentNotificationSendBinding.inflate(layoutInflater, container, false)
        // 툴바 세팅
        settingToolbar()
        return fragmentNotificationSendBinding.root
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNotificationSendBinding.apply {
            toolbarNotificationSend.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_noticeSend_sendNotification -> {
                        removeFragment()
                    }
                }
                true
            }
            toolbarNotificationSend.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentNotificationSendBinding = null
    }

}