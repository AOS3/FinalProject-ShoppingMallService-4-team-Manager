package com.aladin.shoppingmallservice_4_team_manager.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aladin.shoppingmallservice_4_team_manager.databinding.ItemOrderManagerBinding
import com.aladin.shoppingmallservice_4_team_manager.model.OrderManagerModel
import com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager.OrderManagerDetailFragment
import com.aladin.shoppingmallservice_4_team_manager.ui.sellOrderManager.OrderManagerFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceMainFragment
import com.aladin.shoppingmallservice_4_team_manager.util.toCommaString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderManagerAdapter(
    private var orderDataList: List<OrderManagerModel>,
    private val orderManagerFragment: OrderManagerFragment
) :
    RecyclerView.Adapter<OrderManagerAdapter.OrderManagerViewHolder>() {

    fun updateData(newData: List<OrderManagerModel>, value: Int) {
        // value가 0일 경우, orderInquiryDeliveryResult가 1 또는 2인 항목만 필터링
        orderDataList = if (value == 0) {
            newData.filter { it.orderInquiryDeliveryResult == 0 || it.orderInquiryDeliveryResult == 1 }
        } else {
            // 그 외의 경우에는 value와 동일한 orderInquiryDeliveryResult 값을 가진 항목만 필터링
            newData.filter { it.orderInquiryDeliveryResult == value }
        }
        notifyDataSetChanged()
    }

    fun formatOrderTime(orderInquiryTime: Long): String {
        val date = Date(orderInquiryTime)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.KOREAN)
        return "주문 시간 : ${formatter.format(date)}"
    }


    inner class OrderManagerViewHolder(private val itemOrderManagerBinding: ItemOrderManagerBinding) :
        RecyclerView.ViewHolder(itemOrderManagerBinding.root) {
        fun bind(data: OrderManagerModel) {
            itemOrderManagerBinding.apply {
                textViewOrderManagerItemTitle.text = data.orderInquiryTitle
                textViewOrderManagerItemDeliveryResult.text =
                    when (data.orderInquiryDeliveryResult) {
                        0 -> "배송 전"
                        1 -> "배송 중"
                        else -> "배송 완료"
                    }
                textViewOrderManagerItemDeliveryResult.setTextColor(
                    Color.parseColor(
                        when (data.orderInquiryDeliveryResult) {
                            0 -> "#FF0000"
                            1 -> "#FF6A00"
                            else -> "#0064C8"
                        }
                    )
                )
                textViewOrderManagerItemAuthor.text = data.orderInquiryAuthor
                textViewOrderManagerItemOrderTime.text = formatOrderTime(data.orderInquiryTime)
                textViewOrderManagerItemPrice.text =
                    "주문 금액 : ${data.orderInquiryPrice.toCommaString()}원"
                textViewOrderManagerItemQuality.text = "품질 : " + when (data.orderInquiryQuality) {
                    0 -> "상"
                    1 -> "중"
                    else -> "하"
                }
                textViewOrderManagerItemOrderNumber.text = "주문번호 : ${data.orderInquiryNumber}"
            }

            itemView.setOnClickListener {
                val dataBundle = Bundle()
                dataBundle.putString("title", data.orderInquiryTitle)
                dataBundle.putString("author", data.orderInquiryAuthor)
                dataBundle.putInt("quality", data.orderInquiryQuality)
                dataBundle.putString("orderNumber", data.orderInquiryNumber)
                dataBundle.putString("userNumber", data.orderInquiryUserToken)
                dataBundle.putInt("total", data.orderInquiryTotal)
                dataBundle.putLong("time", data.orderInquiryTime)

                orderManagerFragment.replaceMainFragment(
                    OrderManagerDetailFragment(),
                    true,
                    dataBundle = dataBundle
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderManagerViewHolder {
        val itemOrderManagerBinding =
            ItemOrderManagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderManagerViewHolder(itemOrderManagerBinding)
    }

    override fun getItemCount(): Int {
        return orderDataList.size
    }

    override fun onBindViewHolder(holder: OrderManagerViewHolder, position: Int) {
        val data = orderDataList[position]
        holder.bind(data)
    }
}