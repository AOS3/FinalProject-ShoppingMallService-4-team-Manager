package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeEditBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class NoticeEditFragment : Fragment() {

    private var _fragmentNoticeEditBinding: FragmentNoticeEditBinding? = null
    private val fragmentNoticeEditBinding get() = _fragmentNoticeEditBinding!!
    private val noticeViewModel: NoticeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeEditBinding =
            FragmentNoticeEditBinding.inflate(layoutInflater, container, false)

        // 데이터 Set
        settingTextView()

        // 툴바
        settingToolbar()

        // 텍스트 리스너
        settingTextChangeListener()

        // 옵저버
        observeChangeUserData()
        return fragmentNoticeEditBinding.root
    }

    // Observer
    private fun observeChangeUserData() {
        fragmentNoticeEditBinding.apply {
            noticeViewModel.isEditNoticeData.observe(viewLifecycleOwner) { isEdit ->
                if(isEdit) {
                    val result = Bundle().apply {
                        putString("changeRecyclerView","changeRecyclerView")
                    }
                    parentFragmentManager.setFragmentResult("changeRecyclerView",result)
                    removeFragment()
                    removeFragment()
                }
            }
        }
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNoticeEditBinding.apply {
            toolbarNoticeEdit.setNavigationOnClickListener {
                removeFragment()
            }
            toolbarNoticeEdit.setOnMenuItemClickListener {
                when (it.itemId) {
                    else -> {
                        // TextField 검사 진행
                        if (checkTextField()) {
                            // 데이터 수정 진행
                            noticeViewModel.editNoticeData(
                                arguments?.getString("title")!!,
                                arguments?.getString("date")!!,
                                textFieldNoticeEditNoticeTitle.editText?.text.toString(),
                                textFieldNoticeEditNoticeContent.editText?.text.toString(),
                                System.currentTimeMillis().toString()
                            )
                        }
                    }
                }
                true
            }
        }
    }

    // addTextChangedListener
    private fun settingTextChangeListener() {
        fragmentNoticeEditBinding.apply {
            textFieldNoticeEditNoticeTitle.editText?.addTextChangedListener {
                textFieldNoticeEditNoticeTitle.error = null
            }
            textFieldNoticeEditNoticeContent.editText?.addTextChangedListener {
                textFieldNoticeEditNoticeContent.error = null
            }
        }
    }


    // CheckTextField
    private fun checkTextField(): Boolean {
        fragmentNoticeEditBinding.apply {
            if (textFieldNoticeEditNoticeTitle.editText?.text?.isEmpty()!!) {
                textFieldNoticeEditNoticeTitle.error = "제목을 입력해 주세요."
                return false
            }
            if (textFieldNoticeEditNoticeContent.editText?.text?.isEmpty()!!) {
                textFieldNoticeEditNoticeContent.error = "내용을 입력해 주세요."
                return false
            }
            return true
        }
    }

    // settingTextView
    private fun settingTextView() {
        fragmentNoticeEditBinding.apply {
            if (arguments != null) {
                textFieldNoticeEditNoticeTitle.editText?.setText(arguments?.getString("title")!!)
                textFieldNoticeEditNoticeContent.editText?.setText(arguments?.getString("content")!!)
                textViewNoticeEditNoticeWriteDate.text = formatOrderTime(System.currentTimeMillis())
            }
        }
    }

    // format
    private fun formatOrderTime(orderInquiryTime: Long): String {
        val date = Date(orderInquiryTime)
        val formatter = SimpleDateFormat("yy년 MM월 dd일", Locale.KOREAN)
        return "수정 날짜 : ${formatter.format(date)}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentNoticeEditBinding = null
    }
}