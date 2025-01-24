package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceSubFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoticeManagerFragment : Fragment() {
    private var _fragmentNoticeManagerBinding: FragmentNoticeManagerBinding? = null
    private val fragmentNoticeManagerBinding get() = _fragmentNoticeManagerBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeManagerBinding = FragmentNoticeManagerBinding.inflate(layoutInflater, container, false)

        // 툴바
        settingToolbar()

        return fragmentNoticeManagerBinding.root
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNoticeManagerBinding.apply {
            toolbarInventoryManager.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_notice_writeNotice -> {
                        replaceSubFragment(NoticeWriteFragment(), true)
                    }
                }
                true
            }
            toolbarInventoryManager.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentNoticeManagerBinding = null
    }
}