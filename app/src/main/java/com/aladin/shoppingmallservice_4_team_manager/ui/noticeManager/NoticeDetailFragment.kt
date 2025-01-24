package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeDetailBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoticeDetailFragment : Fragment() {
    private var _fragmentNoticeDetailBinding: FragmentNoticeDetailBinding? = null
    private val fragmentNoticeDetailBinding get() = _fragmentNoticeDetailBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeDetailBinding =
            FragmentNoticeDetailBinding.inflate(layoutInflater, container, false)

        // 툴바
        settingToolbar()

        return fragmentNoticeDetailBinding.root
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNoticeDetailBinding.apply {
            toolbarNoticeDetail.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentNoticeDetailBinding = null
    }
}