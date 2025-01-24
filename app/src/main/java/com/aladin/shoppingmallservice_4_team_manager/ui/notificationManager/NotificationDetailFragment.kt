package com.aladin.shoppingmallservice_4_team_manager.ui.notificationManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNotificationDetailBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationDetailFragment : Fragment() {

    private var _fragmentNotificationDetailBinding: FragmentNotificationDetailBinding? = null
    private val fragmentNotificationDetailBinding get() = _fragmentNotificationDetailBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNotificationDetailBinding =
            FragmentNotificationDetailBinding.inflate(layoutInflater, container, false)

        // 툴바 세팅
        settingToolbar()

        return fragmentNotificationDetailBinding.root
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNotificationDetailBinding.apply {
            toolbarNotificationDetail.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentNotificationDetailBinding = null
    }
}