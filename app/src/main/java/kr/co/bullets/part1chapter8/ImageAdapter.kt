package kr.co.bullets.part1chapter8

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.bullets.part1chapter8.databinding.ItemImageBinding
import kr.co.bullets.part1chapter8.databinding.ItemLoadMoreBinding

// ListAdapter는 RecyclerView.Adapter와는 다르게 안에 들어가는 리스트 데이터를 변경하는 것만으로도
// 모든 작업이 알아서 처리됩니다.
// DiffUtil.ItemCallback: 데이터 변경 여부를 확인하기 위한 기능
class ImageAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems>() {
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            // 참조가 같은지
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            // 데이터가 같은지
            return oldItem == newItem
        }
    }
) {
    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else originSize.inc()
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when (viewType) {
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }
            // ITEM_LOAD_MORE
            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
                LoadMoreViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 맨 마지막 아이템에는 값이 없기 때문에 사용할 수 없음
//        when (val item = currentList[position]) {
//            is ImageItems.Image -> TODO()
//            ImageItems.LoadMore -> TODO()
//        }
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }
            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)
            }
        }
    }

    interface ItemClickListener {
        fun onLoadMoreClick()
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }
}

// when 조건문 사용 시, else를 사용하지 않아도 되는 이점이 있습니다.
sealed class ImageItems {
    data class Image(
        val uri: Uri
    ) : ImageItems()

    // 싱글톤으로 바로 객체가 만들어지는 특징이 있습니다.
    object LoadMore : ImageItems()
}

class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageItems.Image) {
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(itemClickListener: ImageAdapter.ItemClickListener) {
        itemView.setOnClickListener {
            itemClickListener.onLoadMoreClick()
        }
    }
}
