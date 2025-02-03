package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeWriteBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import com.aladin.shoppingmallservice_4_team_manager.util.showSoftInput
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeWriteFragment : Fragment() {

    private var _fragmentNoticeWriteBinding: FragmentNoticeWriteBinding? = null
    private val fragmentNoticeManagerBinding get() = _fragmentNoticeWriteBinding!!
    private val noticeViewModel: NoticeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeWriteBinding =
            FragmentNoticeWriteBinding.inflate(layoutInflater, container, false)

        // 화면 입장 시 title focus
        fragmentNoticeManagerBinding.textFieldNoticeWriteNoticeTitle.editText?.showSoftInput()

        // 텍스트 리스너
        settingTextChangeListener()

        // 툴바
        settingToolbar()

        // 등록하기 버튼
        onClickWriteNotice()

        // 옵저버
        observeAddNoticeData()

        return fragmentNoticeManagerBinding.root
    }

    // Observer
    private fun observeAddNoticeData() {
        fragmentNoticeManagerBinding.apply {
            noticeViewModel.isAddNoticeData.observe(viewLifecycleOwner) { isAdd ->
                if (isAdd) {
                    Toast.makeText(requireContext(), "공지사항이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    val result = Bundle().apply {
                        putString("changeRecyclerView","changeRecyclerView")
                    }
                    parentFragmentManager.setFragmentResult("changeRecyclerView",result)
                    removeFragment()
                }
            }
        }
    }

    // addNoticeData
    private fun onClickWriteNotice() {
        fragmentNoticeManagerBinding.apply {
            buttonNoticeWriteWriteNoticeButton.setOnClickListener {
                if (checkTextField()) {
                    noticeViewModel.addNoticeData(
                        textFieldNoticeWriteNoticeTitle.editText?.text.toString(),
                        textFieldNoticeWriteNoticeContent.editText?.text.toString()
                    )
                }
            }
        }
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNoticeManagerBinding.apply {
            toolbarNoticeWrite.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    // addTextChangedListener
    private fun settingTextChangeListener() {
        fragmentNoticeManagerBinding.apply {
            textFieldNoticeWriteNoticeTitle.editText?.addTextChangedListener {
                textFieldNoticeWriteNoticeTitle.error = null
            }
            textFieldNoticeWriteNoticeContent.editText?.addTextChangedListener {
                textFieldNoticeWriteNoticeContent.error = null
            }
        }
    }

    // CheckTextField
    private fun checkTextField(): Boolean {
        fragmentNoticeManagerBinding.apply {
            if (textFieldNoticeWriteNoticeTitle.editText?.text?.isEmpty()!!) {
                textFieldNoticeWriteNoticeTitle.error = "제목을 입력해 주세요."
                return false
            }
            if (textFieldNoticeWriteNoticeContent.editText?.text?.isEmpty()!!) {
                textFieldNoticeWriteNoticeContent.error = "내용을 입력해 주세요."
                return false
            }
            return true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentNoticeWriteBinding = null
    }
}