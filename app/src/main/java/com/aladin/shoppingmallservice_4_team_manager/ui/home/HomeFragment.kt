package com.aladin.shoppingmallservice_4_team_manager.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentHomeBinding
import com.aladin.shoppingmallservice_4_team_manager.ui.askManager.AskManagerFragment
import com.aladin.shoppingmallservice_4_team_manager.ui.inventoryManager.InventoryManagerFragment
import com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager.NoticeManagerFragment
import com.aladin.shoppingmallservice_4_team_manager.ui.notificationManager.NotificationManagerFragment
import com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager.SellOrderManagerFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceSubFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        // 버튼 클릭
        onClickButtonItem()

        return fragmentHomeBinding.root
    }

    // onClickButton
    private fun onClickButtonItem() {
        fragmentHomeBinding.apply {
            // 재고관리
            buttonHomeInventoryManager.setOnClickListener {
                replaceSubFragment(InventoryManagerFragment(), true)
            }
            // 공지사항 관리
            buttonHomeNoticeManager.setOnClickListener {
                replaceSubFragment(NoticeManagerFragment(), true)
            }
            // 1:1 문의 관리
            buttonHomeAskManager.setOnClickListener {
                replaceSubFragment(AskManagerFragment(), true)
            }
            // 알림창 관리
            buttonHomeNotificationManager.setOnClickListener {
                replaceSubFragment(NotificationManagerFragment(), true)
            }
            // 판매/구매 주문 관리
            buttonHomeSellOrderManager.setOnClickListener {
                replaceSubFragment(SellOrderManagerFragment(),true)
            }
            // 회원관리
            buttonHomeUserManager.setOnClickListener {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentHomeBinding = null
    }
}