package com.aladin.shoppingmallservice_4_team_manager.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentMainBinding
import com.aladin.shoppingmallservice_4_team_manager.ui.home.HomeFragment
import com.aladin.shoppingmallservice_4_team_manager.util.clearAllBackStack
import com.aladin.shoppingmallservice_4_team_manager.util.replaceSubFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var fragmentMainBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (::fragmentMainBinding.isInitialized == false) {
            fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater, container, false)
            setBottomNavigationView()
            fragmentMainBinding.bottomAppBarMain.selectedItemId = R.id.nav_home
        }
        return fragmentMainBinding.root
    }

    // 네비게이션 아이콘에 클릭에 따라 화면이 변하게
    private fun setBottomNavigationView() {
        fragmentMainBinding.bottomAppBarMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    clearAllBackStack()
                    replaceSubFragment(HomeFragment(), false)
                }
            }
            true
        }
    }
}