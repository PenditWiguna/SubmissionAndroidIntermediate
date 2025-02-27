package android.me.keenanstory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StoryAdapter(private val listStoryData: ArrayList<StoryData>): RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryData)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.custom_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listStoryData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, userName, avtUrl) = listStoryData[position]
        Glide.with(holder.itemView.context).load(avtUrl).into(holder.imgPhoto)
        holder.tvName.text = userName

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStoryData[holder.adapterPosition])
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}