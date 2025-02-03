package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeDetailBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceMainFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class NoticeDetailFragment : Fragment() {
    private var _fragmentNoticeDetailBinding: FragmentNoticeDetailBinding? = null
    private val fragmentNoticeDetailBinding get() = _fragmentNoticeDetailBinding!!
    private val noticeViewModel: NoticeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeDetailBinding =
            FragmentNoticeDetailBinding.inflate(layoutInflater, container, false)

        // 데이터 Set
        settingTextView()

        // 툴바
        settingToolbar()

        // 공지사항 상태 변경 버튼 클릭 메서드
        onClickHideAndShowButton()

        // 옵저버
        observeAddNoticeData()

        return fragmentNoticeDetailBinding.root
    }

    // Observer
    private fun observeAddNoticeData() {
        fragmentNoticeDetailBinding.apply {
            noticeViewModel.isEditNoticeState.observe(viewLifecycleOwner) { isAdd ->
                if (isAdd) {
                    Toast.makeText(requireContext(), "상태가 정상적으로 변경되었습니다.", Toast.LENGTH_SHORT)
                        .show()

                    val result = Bundle().apply {
                        putString("changeRecyclerView", "changeRecyclerView")
                    }
                    parentFragmentManager.setFragmentResult("changeRecyclerView", result)
                }
            }
        }
    }

    private fun onClickHideAndShowButton() {
        fragmentNoticeDetailBinding.apply {
            buttonNoticeDetailNoticeHide.setOnClickListener { changeNoticeState(1) }
            buttonNoticeDetailNoticeShow.setOnClickListener { changeNoticeState(0) }
        }
    }

    private fun changeNoticeState(value: Int) {
        noticeViewModel.changeNoticeState(
            arguments?.getString("title")!!,
            arguments?.getString("date")!!,
            value
        )
    }

    // settingTextView
    private fun settingTextView() {
        fragmentNoticeDetailBinding.apply {
            if (arguments != null) {
                textViewNoticeDetailNoticeTitle.text = arguments?.getString("title")
                textViewNoticeDetailContent.text = arguments?.getString("content")
                textViewNoticeDetailNoticeDate.text =
                    formatOrderTime(arguments?.getString("date")!!.toLong())
            }
        }
    }

    // format
    private fun formatOrderTime(orderInquiryTime: Long): String {
        val date = Date(orderInquiryTime)
        val formatter = SimpleDateFormat("yy년 MM월 dd일", Locale.KOREAN)
        return "작성일 : ${formatter.format(date)}"
    }

    // toolBar
    private fun settingToolbar() {
        fragmentNoticeDetailBinding.apply {
            toolbarNoticeDetail.setNavigationOnClickListener {
                removeFragment()
            }
            toolbarNoticeDetail.setOnMenuItemClickListener {
                when (it.itemId) {
                    else -> {
                        val dataBundle = Bundle()
                        dataBundle.putString("title", arguments?.getString("title")!!)
                        dataBundle.putString("content", arguments?.getString("content")!!)
                        dataBundle.putString("date", arguments?.getString("date")!!)
                        replaceMainFragment(NoticeEditFragment(), true, dataBundle)
                    }
                }
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentNoticeDetailBinding = null
    }
}