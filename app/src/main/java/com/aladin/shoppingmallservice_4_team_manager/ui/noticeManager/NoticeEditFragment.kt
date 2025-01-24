package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeEditBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoticeEditFragment : Fragment() {

    private var _fragmentNoticeEditBinding: FragmentNoticeEditBinding? = null
    private val fragmentNoticeEditBinding get() = _fragmentNoticeEditBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeEditBinding =
            FragmentNoticeEditBinding.inflate(layoutInflater, container, false)
        return fragmentNoticeEditBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentNoticeEditBinding = null
    }

}