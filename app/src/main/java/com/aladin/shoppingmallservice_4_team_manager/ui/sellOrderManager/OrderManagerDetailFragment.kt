package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentOrderManagerDetailBinding
import com.aladin.shoppingmallservice_4_team_manager.dialog.CustomDialogProgressbar
import com.aladin.shoppingmallservice_4_team_manager.util.removeFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class OrderManagerDetailFragment : Fragment() {

    private var _fragmentOrderManagerDetailBinding: FragmentOrderManagerDetailBinding? = null
    private val fragmentOrderManagerDetailBinding get() = _fragmentOrderManagerDetailBinding!!
    private val orderManagerViewModel: OrderManagerViewModel by viewModels()
    private lateinit var progressBarDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentOrderManagerDetailBinding =
            FragmentOrderManagerDetailBinding.inflate(layoutInflater, container, false)

        // 데이터 set
        settingTextView()

        // 툴바
        settingToolbar()

        // 버튼 클릭
        onClickProcess()

        // 옵저버
        observeChangeUserData()

        return fragmentOrderManagerDetailBinding.root
    }

    // Observer
    private fun observeChangeUserData() {
        orderManagerViewModel.isFinishChangeDeliveryValue.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.dismiss()
            }
        }
    }

    // onClick
    private fun onClickProcess() {
        fragmentOrderManagerDetailBinding.apply {
            // 배송 시작
            buttonOrderManagerDetailDeliveryStart.setOnClickListener {
                changeDeliveryStatusValue(1)
                Toast.makeText(requireContext(),"배송 상태값이 배송 중 으로 변경되었습니다.",Toast.LENGTH_SHORT).show()
            }
            // 배송 완료
            buttonOrderManagerDetailDeliveryFinish.setOnClickListener {
                changeDeliveryStatusValue(2)
                Toast.makeText(requireContext(),"배송 상태값이 배송 완료 으로 변경되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ChangeDeliveryStatus
    private fun changeDeliveryStatusValue(value: Int) {
        orderManagerViewModel.changeDeliveryStatus(
            arguments?.getString("orderNumber")!!,
            arguments?.getLong("time")!!,
            value
        )
        // progressDialog
        progressBarDialog = CustomDialogProgressbar(requireContext())
        progressBarDialog.show()
    }

    // toolBar
    private fun settingToolbar() {
        fragmentOrderManagerDetailBinding.apply {
            toolbarOrderManagerDetail.setNavigationOnClickListener {
                val result = Bundle().apply {
                    putString("changeRecyclerView", "changeRecyclerView")
                }
                parentFragmentManager.setFragmentResult("changeRecyclerView", result)
                removeFragment()

            }
        }
    }

    // set TextView
    private fun settingTextView() {
        fragmentOrderManagerDetailBinding.apply {
            if (arguments != null) {
                textViewOrderManagerDetailTitle.text = arguments?.getString("title")
                textViewOrderManagerDetailAuthor.text = arguments?.getString("author")
                textViewOrderManagerDetailQuality.text =
                    formatQualityNumber(arguments?.getInt("quality")!!)
                textViewOrderManagerDetailUserNumber.text =
                    "회원 번호: ${arguments?.getString("orderNumber")}"
                textViewOrderManagerDetailUserNumber.text =
                    "주문 번호: ${arguments?.getString("userNumber")}"
                textViewOrderManagerDetailTotal.text = "개수: ${arguments?.getInt("total")}개"
                textViewOrderManagerDetailOrderData.text =
                    formatOrderTime(arguments?.getLong("time")!!)
            }
        }
    }

    // format
    private fun formatOrderTime(orderInquiryTime: Long): String {
        val date = Date(orderInquiryTime)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.KOREAN)
        return "등록 날짜 : ${formatter.format(date)}"
    }

    // format
    private fun formatQualityNumber(quality: Int): String {
        return when (quality) {
            0 -> "품질: 상"
            1 -> "품질: 중"
            else -> "품질: 하"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentOrderManagerDetailBinding = null
    }

}