package com.aladin.shoppingmallservice_4_team_manager.ui.inventoryManager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aladin.shoppingmallservice_4_team_manager.databinding.ItemInventoryManagerListBinding
import com.aladin.shoppingmallservice_4_team_manager.model.BookCountModel
import java.text.SimpleDateFormat

class InventoryManagerAdapter: RecyclerView.Adapter<InventoryManagerAdapter.InventoryViewHolder>() {
    private val items = mutableListOf<BookCountModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(ItemInventoryManagerListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(item: MutableList<BookCountModel>) {
        items.clear()
        items.addAll(item)
        notifyDataSetChanged()
    }

    class InventoryViewHolder(
        private val binding: ItemInventoryManagerListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookCountModel) {
            binding.apply {
                textViewInventoryTitle.text = item.bookCountName
                textViewInventoryAuthor.text = item.bookCountAuthor
                textViewInventoryIsbn.text = item.bookCountISBN
                textViewInventoryDate.text = showDateData(item.bookCountTime)
                textViewInventoryCount.text = "재고 : ${item.bookCountTotal}개"
                textViewInventoryQuality.text = when(item.bookCountQuality) {
                    0 -> "품질 : 상"
                    1 -> "품질 : 중"
                    else -> "품질 : 하"
                }
            }
        }

        // 날짜 보여주는 메서드
        private fun showDateData(timeData: Long): String {
            val dataFormat1 = SimpleDateFormat("yyyy년MM월dd일")
            val date = dataFormat1.format(timeData)
            return date
        }
    }
}