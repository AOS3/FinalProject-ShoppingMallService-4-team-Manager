package com.aladin.shoppingmallservice_4_team_manager.ui.inventoryManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentInventoryManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InventoryManagerFragment : Fragment() {
    private var _binding: FragmentInventoryManagerBinding? = null
    private val binding get() = _binding!!

    private val adapter: InventoryManagerAdapter by lazy { InventoryManagerAdapter() }

    private val viewModel: InventoryManagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventoryManagerBinding.inflate(layoutInflater, container, false)
        // 툴바 세팅
        loadList()
        settingToolbar()
        settingRecyclerView()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    툴바
     */
    private fun settingToolbar() {
        binding.apply {
            toolbarInventoryManager.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    /*
    리사이클러 뷰
     */

    private fun settingRecyclerView() {
        binding.recyclerViewInventoryManager.adapter = adapter

        viewModel.inventoryList.observe(viewLifecycleOwner) {
            adapter.submitList(it.sortedByDescending { it.bookCountTime }.toMutableList())
        }
    }

    private fun loadList() {
        viewModel.loadList()
    }


}