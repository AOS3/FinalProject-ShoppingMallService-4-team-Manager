package com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aladin.shoppingmallservice_4_team_manager.Model.SellingInquiryModel
import com.aladin.shoppingmallservice_4_team_manager.R
import com.aladin.shoppingmallservice_4_team_manager.databinding.FragmentSellManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.databinding.RowBookSellManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.util.replaceMainFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SellManagerFragment : Fragment() {

    private var _fragmentSellManagerBinding: FragmentSellManagerBinding? = null
    private val fragmentSellManagerBinding get() = _fragmentSellManagerBinding!!

    private lateinit var adapter: RecyclerSellManagerAdapter
    private val sellingCartViewModel: SellManagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentSellManagerBinding =
            FragmentSellManagerBinding.inflate(layoutInflater, container, false)

        // RecyclerView 설정 메서드 호츌
        settingRecyclerView()

        // 드롭다운 메뉴 설정 메서드 호출
        settingDropMenu()

        // 관찰 메서드 호출
        observeViewModel()

        return fragmentSellManagerBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentSellManagerBinding = null
    }

    // 관찰 메서드
    private fun observeViewModel() {
        sellingCartViewModel.filteredInquiries.observe(viewLifecycleOwner) { inquiries ->
            adapter.updateData(inquiries)
        }
    }

    // 드롭다운 메뉴 설정 메서드
    private fun settingDropMenu() {
        fragmentSellManagerBinding.apply {
            // 드롭다운 아이템 목록
            val sortOptions = arrayOf("검수 중", "검수 완료")

            // 어댑터 설정
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
            autoCompleteTextViewSellManagerSortOrder.setAdapter(adapter)

            // 드롭다운 선택 시 동작
            autoCompleteTextViewSellManagerSortOrder.setOnItemClickListener { _, _, position, _ ->
                when (sortOptions[position]) {
                    "검수 중" -> sellingCartViewModel.filterInquiries(0)
                    "검수 완료" -> sellingCartViewModel.filterInquiries(1)
                }
            }

            // 드롭다운이 닫혔을 때 화살표 모양 바뀌게
            autoCompleteTextViewSellManagerSortOrder.setOnDismissListener {
                imageViewSellManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
            }

            // 드롭다운 열리게 설정, 화살표 변경 / 이미 열려있으면 닫히게 설정
            linearLayoutSellManagerSortState.setOnClickListener {
                if (!autoCompleteTextViewSellManagerSortOrder.isPopupShowing) { // 드롭다운이 열려 있는지 확인
                    autoCompleteTextViewSellManagerSortOrder.showDropDown()
                    imageViewSellManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_up_24px)
                } else {
                    autoCompleteTextViewSellManagerSortOrder.dismissDropDown() // 이미 열려 있으면 닫기
                    imageViewSellManagerDropDownIcon.setImageResource(R.drawable.arrow_drop_down_24px)
                }
            }
        }
    }

    // 세 자리마다 콤마 추가하는 함수
    private fun formatNumber(number: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }

    // RecyclerView 설정 메서드
    private fun settingRecyclerView() {
        adapter = RecyclerSellManagerAdapter(emptyList()).apply {
            // 아이템 클릭 이벤트 설정
            onItemClick = { selectedItem ->
                val detailFragment = SellManagerDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString("documentId", selectedItem.documentId)
                    }
                }
                replaceMainFragment(detailFragment, true)
            }

        }
        fragmentSellManagerBinding.recyclerViewSellManager.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SellManagerFragment.adapter
            addItemDecoration(MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    // RecyclerView 어댑터
    private inner class RecyclerSellManagerAdapter(
        private var items: List<SellingInquiryModel>
    ) : RecyclerView.Adapter<RecyclerSellManagerAdapter.ViewHolder>() {

        // 클릭 리스너 정의
        var onItemClick: ((SellingInquiryModel) -> Unit)? = null

        inner class ViewHolder(private val rowBookSellManagerBinding: RowBookSellManagerBinding) : RecyclerView.ViewHolder(rowBookSellManagerBinding.root) {
            fun bind(item: SellingInquiryModel) {

            if (item.sellingInquiryOriginalPrice == 0) {
                sellingCartViewModel.saveOriginalPriceIfNeeded(item.documentId, item.sellingInquiryPrice, item.sellingInquiryQuality)
            }

                // 상태에 따른 텍스트와 색상 설정
                val state = when (item.sellingInquiryApprovalResult) {
                    0 -> {
                        "승인 신청"
                    }
                    1 -> {
                        rowBookSellManagerBinding.textViewSellManagerState.setTextColor(
                            ContextCompat.getColor(rowBookSellManagerBinding.root.context, R.color.orange_color)
                        ) // FF6A00
                        rowBookSellManagerBinding.textViewSellManagerQuality.setTextColor(
                            ContextCompat.getColor(rowBookSellManagerBinding.root.context, R.color.red_color)
                        )
                        "품질 검수 중"
                    }
                    2 -> {
                        rowBookSellManagerBinding.textViewSellManagerState.setTextColor(
                            ContextCompat.getColor(rowBookSellManagerBinding.root.context, R.color.blue_color)
                        ) // 0064C8
                        rowBookSellManagerBinding.textViewSellManagerQuality.setTextColor(
                            ContextCompat.getColor(rowBookSellManagerBinding.root.context, R.color.green_color)
                        ) // 32BE07
                        "검수 완료"
                    }
                    else -> "오류"
                }

                // 상태 텍스트 설정
                rowBookSellManagerBinding.textViewSellManagerState.text = state

                // 품질 설정
                val quality = when (item.sellingInquiryQuality) {
                    0 -> "상"
                    1 -> "중"
                    2 -> "하"
                    3 -> "매입 불가"
                    else -> "오류"
                }

                // 검수 완료 상태에서 직원 품질도 추가로 표시
                val qualityText = if (item.sellingInquiryApprovalResult == 2) {
                    val staffQuality = when (item.sellingInquiryChoiceQuality) {
                        0 -> "상"
                        1 -> "중"
                        2 -> "하"
                        3 -> "매입 불가"
                        else -> "오류"
                    }
                    "품질: $quality -> $staffQuality"
                } else {
                    "고객 님이 선택한 품질 : $quality"
                }
                rowBookSellManagerBinding.textViewSellManagerQuality.text = qualityText
                rowBookSellManagerBinding.textViewSellManagerBookName.text = item.sellingInquiryBookName
                rowBookSellManagerBinding.textViewSellManagerAuthor.text = item.sellingInquiryBookAuthor
                rowBookSellManagerBinding.textViewSellManagerCustomerNumber.text = "회원 번호 : ${item.sellingInquiryUserToken}"
                val priceText = if (item.sellingInquiryApprovalResult == 2) {
                    "판매가 : ${formatNumber(item.sellingInquiryPrice)}원 -> ${formatNumber(item.sellingInquiryFinalPrice)}원"
                } else {
                    "예상 판매가 : ${formatNumber(item.sellingInquiryPrice)}원"
                }
                rowBookSellManagerBinding.textViewSellManagerPrice.text = priceText

                // 날짜 형식 변환
                val formattedDate = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(
                    Date(item.sellingInquiryTime)
                )
                rowBookSellManagerBinding.textViewSellManagerDate.text = "등록 날짜 : $formattedDate"


                // 아이템 클릭 리스너 설정
                rowBookSellManagerBinding.root.setOnClickListener {
                    onItemClick?.invoke(item) // 클릭된 데이터를 전달
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = RowBookSellManagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        fun updateData(newItems: List<SellingInquiryModel>) {
            items = newItems.sortedByDescending { it.sellingInquiryTime }
            notifyDataSetChanged()
        }
    }
}