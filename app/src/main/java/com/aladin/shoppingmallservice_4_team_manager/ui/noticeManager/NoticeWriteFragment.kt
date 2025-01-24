package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeWriteBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeWriteFragment : Fragment() {

    private var _fragmentNoticeWriteBinding: FragmentNoticeWriteBinding? = null
    private val fragmentNoticeManagerBinding get() = _fragmentNoticeWriteBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeWriteBinding =
            FragmentNoticeWriteBinding.inflate(layoutInflater, container, false)

        // 툴바
        settingToolbar()

        return fragmentNoticeManagerBinding.root
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNoticeManagerBinding.apply {
            toolbarNoticeWrite.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentNoticeWriteBinding = null
    }
}