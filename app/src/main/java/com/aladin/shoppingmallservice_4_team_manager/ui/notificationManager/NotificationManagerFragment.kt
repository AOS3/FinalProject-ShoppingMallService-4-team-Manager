package com.aladin.shoppingmallservice_4_team_manager.ui.notificationManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNotificationManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceSubFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationManagerFragment : Fragment() {

    private var _fragmentNotificationManagerBinding: FragmentNotificationManagerBinding? = null
    private val fragmentNotificationManagerBinding get() = _fragmentNotificationManagerBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNotificationManagerBinding =
            FragmentNotificationManagerBinding.inflate(layoutInflater, container, false)

        // 툴바 세팅
        settingToolbar()
        return fragmentNotificationManagerBinding.root

    }

    // toolBar
    private fun settingToolbar() {
        fragmentNotificationManagerBinding.apply {
            toolbarNotificationManager.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_notice_writeNotice -> {
                        replaceSubFragment(NotificationSendFragment(), true)
                    }
                }
                true
            }
            toolbarNotificationManager.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _fragmentNotificationManagerBinding = null
    }
}