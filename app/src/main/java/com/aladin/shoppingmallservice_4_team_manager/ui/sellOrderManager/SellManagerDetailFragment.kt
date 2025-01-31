package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.aladin.shoppingmallservice_4_team_manager.Model.SellingInquiryModel
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentSellManagerDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SellManagerDetailFragment : Fragment() {

    private var _fragmentSellManagerDetailBinding: FragmentSellManagerDetailBinding? = null
    private val fragmentSellManagerDetailBinding get() = _fragmentSellManagerDetailBinding!!

    private val sellingCartViewModel: SellManagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentSellManagerDetailBinding =
            FragmentSellManagerDetailBinding.inflate(layoutInflater, container, false)

        // Toolbar를 구성하는 메서드 호출
        settingToolbar()

        // 관찰 데이터 메서드 호출
        observeViewModel()

        // 버튼 설정 메서드 호출
        buttonSetting()

        return fragmentSellManagerDetailBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentSellManagerDetailBinding = null
    }

    // Toolbar를 구성하는 메서드
    private fun settingToolbar() {
        fragmentSellManagerDetailBinding.apply {
            materialToolbarSellManagerDetail.setNavigationIcon(R.drawable.arrow_back_ios_24px)
            materialToolbarSellManagerDetail.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    // 관찰 데이터 메서드
    private fun observeViewModel() {
        val documentId = arguments?.getString("documentId") ?: return

        // Firestore 데이터 실시간 감지
        sellingCartViewModel.observeSellingInquiryById(documentId)

        // UI 자동 업데이트
        sellingCartViewModel.selectedInquiry.observe(viewLifecycleOwner) { inquiry ->
            inquiry?.let { updateUI(it) }
        }
    }

    // 버튼 설정 메서드
    private fun buttonSetting() {
        fragmentSellManagerDetailBinding.apply {
            buttonSellManagerDetailState1Start.setOnClickListener {
                sellingCartViewModel.selectedInquiry.value?.let { inquiry ->
                    sellingCartViewModel.updateApprovalResultTo1(inquiry.documentId)
                    Toast.makeText(requireContext(), "품질 검수 중", Toast.LENGTH_SHORT).show()
                }
            }

            buttonSellManagerDetailState2High.setOnClickListener {
                updateApprovalAndSaveInventory(0)
            }

            buttonSellManagerDetailState2Middle.setOnClickListener {
                updateApprovalAndSaveInventory(1)
            }

            buttonSellManagerDetailState2Low.setOnClickListener {
                updateApprovalAndSaveInventory(2)
            }

            buttonSellManagerDetailState2No.setOnClickListener {
                updateApprovalAndSaveInventory(3)
            }
        }
    }

    private fun updateApprovalAndSaveInventory(choiceQuality: Int) {
        sellingCartViewModel.selectedInquiry.value?.let { inquiry ->
            // 승인 상태 업데이트
            sellingCartViewModel.updateApprovalResultTo2(inquiry.documentId, choiceQuality)

            // 최종 가격 업데이트 후, Firestore 업데이트 완료 시 최신 데이터 가져오기
            sellingCartViewModel.updateFinalPriceAndWait(inquiry.documentId, choiceQuality) { updatedInquiry ->
                updatedInquiry?.let {
                    sellingCartViewModel.insertNotification(it)
                    if(choiceQuality == 0 || choiceQuality == 1 || choiceQuality == 2) {
                        sellingCartViewModel.insertUsedBookInventory(it)
                        sellingCartViewModel.insertBookCount(it)
                    }
                }
            }
        }
    }

    private fun updateUI(inquiry: SellingInquiryModel) {
        fragmentSellManagerDetailBinding.apply {
            textViewSellManagerDetailBookName.text = inquiry.sellingInquiryBookName
            textViewSellManagerDetailAuthor.text = inquiry.sellingInquiryBookAuthor

            val qualityText = if (inquiry.sellingInquiryApprovalResult == 2) {
                val staffQuality = getQualityText(inquiry.sellingInquiryChoiceQuality)
                "품질: ${getQualityText(inquiry.sellingInquiryQuality)} -> $staffQuality"
            } else {
                "고객님이 선택한 품질: ${getQualityText(inquiry.sellingInquiryQuality)}"
            }
            textViewSellManagerDetailQuality.text = qualityText

            textViewSellManagerDetailUserId.text = "회원 번호 : ${inquiry.sellingInquiryUserToken}"
            textViewSellManagerDetailDepositor.text = "예금주 : ${inquiry.sellingInquiryDepositor}"
            textViewSellManagerDetailBank.text = "은행명 : ${inquiry.sellingInquiryBankName}"
            textViewSellManagerDetailAccount.text = "계좌 번호 : ${inquiry.sellingInquiryBankAccountNumber}"

            val priceText = if (inquiry.sellingInquiryApprovalResult == 2) {
                "판매가: ${formatNumber(inquiry.sellingInquiryPrice)}원 -> ${formatNumber(inquiry.sellingInquiryFinalPrice)}원"
            } else {
                "예상 판매가: ${formatNumber(inquiry.sellingInquiryPrice)}원"
            }
            textViewSellManagerDetailPrice.text = priceText

            textViewSellManagerDetailDate.text = "등록 날짜: ${formatDate(inquiry.sellingInquiryTime)}"
            textViewSellManagerDetailState.text = getStateText(inquiry.sellingInquiryApprovalResult)
            textViewSellManagerDetailState.setTextColor(getStateColor(inquiry.sellingInquiryApprovalResult, root.context))

            // 승인 상태에 따른 버튼 활성화/비활성화 처리
            when (inquiry.sellingInquiryApprovalResult) {
                0 -> {
                    setButtonState(buttonSellManagerDetailState1Start, true)
                    setButtonState(buttonSellManagerDetailState2High, false)
                    setButtonState(buttonSellManagerDetailState2Middle, false)
                    setButtonState(buttonSellManagerDetailState2Low, false)
                    setButtonState(buttonSellManagerDetailState2No, false)
                }
                1 -> {
                    setButtonState(buttonSellManagerDetailState1Start, false)
                    setButtonState(buttonSellManagerDetailState2High, true)
                    setButtonState(buttonSellManagerDetailState2Middle, true)
                    setButtonState(buttonSellManagerDetailState2Low, true)
                    setButtonState(buttonSellManagerDetailState2No, true)
                }
                2 -> {
                    setButtonState(buttonSellManagerDetailState1Start, false)
                    setButtonState(buttonSellManagerDetailState2High, false)
                    setButtonState(buttonSellManagerDetailState2Middle, false)
                    setButtonState(buttonSellManagerDetailState2Low, false)
                    setButtonState(buttonSellManagerDetailState2No, false)
                }
            }
        }
    }

    // 버튼 활성화/비활성화에 따른 alpha 설정 추가
    private fun setButtonState(button: View, isEnabled: Boolean) {
        button.isEnabled = isEnabled

//        if (button is androidx.appcompat.widget.AppCompatButton) {
//            button.setTextColor(ContextCompat.getColor(button.context, R.color.white))
//        }
    }

    // 세 자리마다 콤마 추가하는 함수
    private fun formatNumber(number: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }

    // 품질 상태 텍스트 변환
    private fun getQualityText(quality: Int): String {
        return when (quality) {
            0 -> "상"
            1 -> "중"
            2 -> "하"
            3 -> "매입 불가"
            else -> "오류"
        }
    }

    // 승인 상태 텍스트 변환
    private fun getStateText(state: Int): String {
        return when (state) {
            0 -> "승인 신청"
            1 -> "품질 검수 중"
            2 -> "검수 완료"
            else -> "오류"
        }
    }

    // 승인 상태에 따른 색상 변환
    private fun getStateColor(state: Int, context: Context): Int {
        return when (state) {
            1 -> ContextCompat.getColor(context, R.color.orange_color)
            2 -> ContextCompat.getColor(context, R.color.blue_color)
            else -> ContextCompat.getColor(context, R.color.red_color)
        }
    }

    // 날짜 포맷팅
    private fun formatDate(time: Long): String {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        return dateFormat.format(Date(time))
    }
}