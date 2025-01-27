package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentSellOrderManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceSellOrderFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellOrderManagerFragment : Fragment() {

    private var _fragmentSellOrderManagerBinding: FragmentSellOrderManagerBinding? = null
    private val fragmentSellOrderManagerBinding get() = _fragmentSellOrderManagerBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentSellOrderManagerBinding =
            FragmentSellOrderManagerBinding.inflate(layoutInflater, container, false)

        // 들어왔을 때 판매로 세팅
        replaceSellOrderFragment(SellManagerFragment(),false)

        // 탭 레이아웃 세팅
        settingTabLayout()

        // 툴바 세팅
        settingToolbar()

        return fragmentSellOrderManagerBinding.root
    }

    // toolBar
    private fun settingToolbar() {
        fragmentSellOrderManagerBinding.apply {
            toolbarSellOrderManager.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    // tabLayout
    private fun settingTabLayout() {
        fragmentSellOrderManagerBinding.apply {
            tabLayoutSellOrderManager.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                  when(tab?.position) {
                      // 판매
                      0 -> {
                          replaceSellOrderFragment(SellManagerFragment(),false)
                      }
                      // 구매
                      1 -> {
                          replaceSellOrderFragment(OrderManagerFragment(),false)
                      }
                  }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                  when(tab?.position) {
                      // 판매
                      0 -> {
                          replaceSellOrderFragment(SellManagerFragment(),false)
                      }
                      // 구매
                      1 -> {
                          replaceSellOrderFragment(OrderManagerFragment(),false)
                      }
                  }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentSellOrderManagerBinding = null
    }
}