package com.aladin.shoppingmallservice_4_team_manager.adapter


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aladin.shoppingmallservice_4_team_manager.databinding.ItemNoticeBinding
import com.aladin.shoppingmallservice_4_team_manager.model.NoticeModel
import com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager.NoticeDetailFragment
import com.aladin.shoppingmallservice_4_team_manager.ui.noticeManager.NoticeManagerFragment
import com.aladin.shoppingmallservice_4_team_manager.util.replaceMainFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoticeAdapter(
    private var noticeList: List<NoticeModel>,
    private val noticeManagerFragment: NoticeManagerFragment
) :
    RecyclerView.Adapter<NoticeAdapter.NoticeManagerViewHolder>() {

    fun updateData(newData: List<NoticeModel>) {
        noticeList = newData
        notifyDataSetChanged()
    }

    fun formatOrderTime(orderInquiryTime: Long): String {
        val date = Date(orderInquiryTime)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
        return formatter.format(date)
    }


    inner class NoticeManagerViewHolder(private val itemNoticeBinding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(itemNoticeBinding.root) {
        fun bind(data: NoticeModel) {
            itemNoticeBinding.apply {
                textViewNoticeItemTitle.text = data.noticeTitle
                textViewNoticeItemDate.text = formatOrderTime(data.noticeDate.toLong())
                if (data.noticeState == 0) {
                    textViewNoticeItemTitle.setTextColor(Color.BLACK)
                    textViewNoticeItemDate.setTextColor(Color.BLACK)
                }else {
                    textViewNoticeItemTitle.setTextColor(Color.GRAY)
                    textViewNoticeItemDate.setTextColor(Color.GRAY)
                }

            }

            itemView.setOnClickListener {
                val dataBundle = Bundle()
                dataBundle.putString("title", data.noticeTitle)
                dataBundle.putString("content", data.noticeContent)
                dataBundle.putString("date", data.noticeDate)

                noticeManagerFragment.replaceMainFragment(
                    NoticeDetailFragment(),
                    true,
                    dataBundle = dataBundle
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeManagerViewHolder {
        val itemNoticeBinding =
            ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeManagerViewHolder(itemNoticeBinding)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onBindViewHolder(holder: NoticeManagerViewHolder, position: Int) {
        val data = noticeList[position]
        holder.bind(data)
    }
}