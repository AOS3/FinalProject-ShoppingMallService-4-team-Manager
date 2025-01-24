package com.aladin.shoppingmallservice_4_team_manager.ui.inventoryManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentInventoryManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InventoryManagerFragment : Fragment() {
    private var _fragmentInventoryManagerBinding: FragmentInventoryManagerBinding? = null
    private val fragmentInventoryManagerBinding get() = _fragmentInventoryManagerBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentInventoryManagerBinding =
            FragmentInventoryManagerBinding.inflate(layoutInflater, container, false)
        // 툴바 세팅
        settingToolbar()

        return fragmentInventoryManagerBinding.root
    }

    private fun settingToolbar() {
        fragmentInventoryManagerBinding.apply {
            toolbarInventoryManager.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentInventoryManagerBinding = null
    }

}