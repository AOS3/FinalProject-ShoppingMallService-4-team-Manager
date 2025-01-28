package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.adapter.OrderManagerAdapter
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentOrderManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.dialog.CustomDialogProgressbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderManagerFragment : Fragment() {

    private var _fragmentOrderManagerBinding: FragmentOrderManagerBinding? = null
    private val fragmentOrderManagerBinding get() = _fragmentOrderManagerBinding!!
    private val orderManagerViewModel: OrderManagerViewModel by viewModels()
    private lateinit var orderMangerAdapter: OrderManagerAdapter
    private lateinit var progressBarDialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 데이터 가져오기
        orderManagerViewModel.gettingUserOrderInquiryData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentOrderManagerBinding =
            FragmentOrderManagerBinding.inflate(layoutInflater, container, false)

        // 화면 입장 시 다이얼로그 Show
        progressBarDialog = CustomDialogProgressbar(requireContext())
        progressBarDialog.show()

        // 리싸이클러뷰
        setupRecyclerView()

        // 드롭 다운
        settingDropMenu()

        // 데이터 옵저버
        observeUserOrderData()

        return fragmentOrderManagerBinding.root
    }

    // Observer
    private fun observeUserOrderData() {
        fragmentOrderManagerBinding.apply {
            orderManagerViewModel.userOrderList.observe(viewLifecycleOwner) { userOrderList ->
                if(userOrderList.isNotEmpty()) {
                    orderMangerAdapter.updateData(userOrderList,0)
                }
            }
            orderManagerViewModel.isLoadUserOrderInquiryData.observe(viewLifecycleOwner) {
                if (it) {
                    progressBarDialog.dismiss()
                }
            }
        }
    }

    // RecyclerView
    private fun setupRecyclerView() {
        fragmentOrderManagerBinding.apply {
            orderMangerAdapter =
                OrderManagerAdapter(emptyList(), this@OrderManagerFragment)
            recyclerViewOrderManager.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewOrderManager.adapter = orderMangerAdapter
        }
    }

    // DropDown
    private fun settingDropMenu() {
        fragmentOrderManagerBinding.apply {
            // 드롭다운 아이템 목록
            val sortOptions = arrayOf("배송 전", "배송 후")

            // 어댑터 설정
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
            autoCompleteTextViewOrderManagerSortOrder.setAdapter(adapter)

            // 드롭다운 선택 시 동작
            autoCompleteTextViewOrderManagerSortOrder.setOnItemClickListener { _, _, position, _ ->
                when (sortOptions[position]) {
                    "배송 전" -> {
                        orderManagerViewModel.userOrderList.value?.let {
                            orderMangerAdapter.updateData(
                                it.toList(),0)
                        }
                        imageViewOrderManagerDropDownText.text = "배송 전"
                    }

                    "배송 후" -> {
                        orderManagerViewModel.userOrderList.value?.let {
                            orderMangerAdapter.updateData(
                                it.toList(),2)
                        }
                        imageViewOrderManagerDropDownText.text = "배송 후"
                    }
                }
            }
            // 드롭다운이 닫혔을 때 화살표 모양 바뀌게
            autoCompleteTextViewOrderManagerSortOrder.setOnDismissListener {
                imageViewOrderManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
            }

            // 드롭다운 열리게 설정, 화살표 변경 / 이미 열려있으면 닫히게 설정
            linearLayoutOrderManagerSortOrder.setOnClickListener {
                if (!autoCompleteTextViewOrderManagerSortOrder.isPopupShowing) { // 드롭다운이 열려 있는지 확인
                    autoCompleteTextViewOrderManagerSortOrder.showDropDown()
                    imageViewOrderManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_up_24px)
                } else {
                    autoCompleteTextViewOrderManagerSortOrder.dismissDropDown() // 이미 열려 있으면 닫기
                    imageViewOrderManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentOrderManagerBinding = null
    }

}