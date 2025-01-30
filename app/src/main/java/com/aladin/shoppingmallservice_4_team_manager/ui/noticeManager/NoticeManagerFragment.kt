package com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.adapter.NoticeAdapter
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentNoticeManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.dialog.CustomDialogProgressbar
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceSubFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoticeManagerFragment : Fragment() {
    private var _fragmentNoticeManagerBinding: FragmentNoticeManagerBinding? = null
    private val fragmentNoticeManagerBinding get() = _fragmentNoticeManagerBinding!!
    private val noticeViewModel: NoticeViewModel by viewModels()
    private lateinit var noticeAdapter: NoticeAdapter
    private lateinit var progressBarDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 데이터 가져오기
        noticeViewModel.gettingNoticeInquiryData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentNoticeManagerBinding =
            FragmentNoticeManagerBinding.inflate(layoutInflater, container, false)

        // 화면 입장 시 다이얼로그 Show
        progressBarDialog = CustomDialogProgressbar(requireContext())
        progressBarDialog.show()

        // 리싸이클러뷰
        setupRecyclerView()

        // 데이터 옵저버
        observeUserOrderData()

        // 툴바
        settingToolbar()

        // 전체 데이터 다시 불러오기
        refreshRecyclerViewData()



        return fragmentNoticeManagerBinding.root
    }

    // 전체 데이터 다시 불러오기
    private fun refreshRecyclerViewData() {
        parentFragmentManager.setFragmentResultListener("changeRecyclerView",viewLifecycleOwner) { _, bundle ->
            val result = bundle.getString("changeRecyclerView")
            if (result != null) {
                noticeViewModel.gettingNoticeInquiryData()
            }
        }
    }

    // Observer
    private fun observeUserOrderData() {
        fragmentNoticeManagerBinding.apply {
            noticeViewModel.noticeList.observe(viewLifecycleOwner) { userOrderList ->
                if(userOrderList.isNotEmpty()) {
                    noticeAdapter.updateData(userOrderList)
                    textViewInventoryManagerEmptyNotice.visibility = View.GONE
                }else {
                    textViewInventoryManagerEmptyNotice.visibility = View.VISIBLE
                }
            }
            noticeViewModel.isLoadNoticeData.observe(viewLifecycleOwner) {
                if (it) {
                    progressBarDialog.dismiss()
                }
            }
        }
    }

    // RecyclerView
    private fun setupRecyclerView() {
        fragmentNoticeManagerBinding.apply {
            noticeAdapter =
                NoticeAdapter(emptyList(), this@NoticeManagerFragment)
            recyclerViewInventoryManager.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewInventoryManager.adapter = noticeAdapter
        }
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