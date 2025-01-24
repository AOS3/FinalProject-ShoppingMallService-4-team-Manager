package com.aladin.shoppingmallservice_4_team_manager.ui.askManager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentAskManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskManagerFragment : Fragment() {

    private var _fragmentAskManagerBinding: FragmentAskManagerBinding? = null
    private val fragmentAskManagerBinding get() = _fragmentAskManagerBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentAskManagerBinding =
            FragmentAskManagerBinding.inflate(layoutInflater, container, false)

        // 툴바 세팅
        settingToolbar()

        // 드롭다운세팅
        settingDropMenu()

        return fragmentAskManagerBinding.root
    }

    // toolBar
    private fun settingToolbar() {
        fragmentAskManagerBinding.apply {
            toolbarAskManager.setNavigationOnClickListener {
                removeFragment()
            }
        }
    }

    // DropDown
    private fun settingDropMenu() {
        fragmentAskManagerBinding.apply {
            // 드롭다운 아이템 목록
            val sortOptions = arrayOf("답변전", "답변후", )

            // 어댑터 설정
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
            autoCompleteTextViewAskManagerSortOrder.setAdapter(adapter)

            // 드롭다운 선택 시 동작
            autoCompleteTextViewAskManagerSortOrder.setOnItemClickListener { _, _, position, _ ->
                when (sortOptions[position]) {
                    "답변전" -> {
                        renewalRecyclerViewFromDropDown(1)
                        imageViewAskManagerDropDownText.text = "답변전"
                    }

                    "답변후" -> {
                        renewalRecyclerViewFromDropDown(2)
                        imageViewAskManagerDropDownText.text = "답변후"
                    }
                }
            }
            // 드롭다운이 닫혔을 때 화살표 모양 바뀌게
            autoCompleteTextViewAskManagerSortOrder.setOnDismissListener {
                imageViewAskManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
            }

            // 드롭다운 열리게 설정, 화살표 변경 / 이미 열려있으면 닫히게 설정
            linearLayoutAskManagerSortOrder.setOnClickListener {
                if (!autoCompleteTextViewAskManagerSortOrder.isPopupShowing) { // 드롭다운이 열려 있는지 확인
                    autoCompleteTextViewAskManagerSortOrder.showDropDown()
                    imageViewAskManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_up_24px)
                } else {
                    autoCompleteTextViewAskManagerSortOrder.dismissDropDown() // 이미 열려 있으면 닫기
                    imageViewAskManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
                }
            }
        }
    }

    // 드롭다운 선택에 따라 recyclerView가 갱신
    private fun renewalRecyclerViewFromDropDown(dropDownValue: Int) {
        fragmentAskManagerBinding.apply {
            when (dropDownValue) {
                1 -> Log.e("asd","답변전 어뎁터 처리")
                else -> Log.e("asd","답변후 어뎁터 처리")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentAskManagerBinding = null
    }


}