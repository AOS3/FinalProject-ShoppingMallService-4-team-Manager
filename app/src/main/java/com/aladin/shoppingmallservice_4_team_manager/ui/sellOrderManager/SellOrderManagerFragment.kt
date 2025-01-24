package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentSellOrderManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
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

        // 드롭다운 세팅
        settingDropMenu()

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

    // DropDown
    private fun settingDropMenu() {
        fragmentSellOrderManagerBinding.apply {
            // 드롭다운 아이템 목록
            val sortOptions = arrayOf("검수중", "검수완료")

            // 어댑터 설정
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
            autoCompleteTextViewSellOrderManagerSortOrder.setAdapter(adapter)

            // 드롭다운 선택 시 동작
            autoCompleteTextViewSellOrderManagerSortOrder.setOnItemClickListener { _, _, position, _ ->
                when (sortOptions[position]) {
                    "검수중" -> {
                        renewalRecyclerViewFromDropDown(1)
                        imageViewSellOrderManagerDropDownText.text = "검수중"
                    }

                    "검수완료" -> {
                        renewalRecyclerViewFromDropDown(2)
                        imageViewSellOrderManagerDropDownText.text = "검수완료"
                    }
                }
            }
            // 드롭다운이 닫혔을 때 화살표 모양 바뀌게
            autoCompleteTextViewSellOrderManagerSortOrder.setOnDismissListener {
                imageViewSellOrderManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
            }

            // 드롭다운 열리게 설정, 화살표 변경 / 이미 열려있으면 닫히게 설정
            linearLayoutSellOrderManagerSortOrder.setOnClickListener {
                if (!autoCompleteTextViewSellOrderManagerSortOrder.isPopupShowing) { // 드롭다운이 열려 있는지 확인
                    autoCompleteTextViewSellOrderManagerSortOrder.showDropDown()
                    imageViewSellOrderManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_up_24px)
                } else {
                    autoCompleteTextViewSellOrderManagerSortOrder.dismissDropDown() // 이미 열려 있으면 닫기
                    imageViewSellOrderManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
                }
            }
        }
    }

    // 드롭다운 선택에 따라 recyclerView가 갱신
    private fun renewalRecyclerViewFromDropDown(dropDownValue: Int) {
        fragmentSellOrderManagerBinding.apply {
            when (dropDownValue) {
                1 -> Log.e("asd", "검수중 어뎁터 처리")
                else -> Log.e("asd", "검수완료 어뎁터 처리")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentSellOrderManagerBinding = null
    }
}