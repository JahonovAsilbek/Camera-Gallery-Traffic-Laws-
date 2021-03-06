package uz.revolution.trafficlaws.main.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_traffic.view.*
import uz.revolution.trafficlaws.R
import uz.revolution.trafficlaws.models.Traffic
import java.io.File


class ItemTrafficAdapter : RecyclerView.Adapter<ItemTrafficAdapter.VH>() {

    private var data: ArrayList<Traffic>? = null
    private var onItemClick: OnItemClick? = null
    private var onEditClick: OnEditClick? = null
    private var onDeleteClick: OnDeleteClick? = null
    private var onLikeClick: OnLikeClick? = null


    fun setAdapter(data: ArrayList<Traffic>) {
        this.data = data
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(traffic: Traffic, position: Int) {
            itemView.name.text = traffic.name
            if (traffic.liked!!) {
                itemView.liked.setImageResource(R.drawable.ic_heart1)
            } else {
                itemView.liked.setImageResource(R.drawable.ic_heart11)
            }

            val imgFile = File(traffic.imagePath)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
                itemView.image.setImageBitmap(myBitmap)
            }

            itemView.setOnClickListener {
                if (onItemClick != null) {
                    onItemClick!!.onClick(traffic, position)
                }
            }

            itemView.edit.setOnClickListener {
                if (onEditClick != null) {
                    onEditClick!!.onClick(traffic, position)
                }
            }

            itemView.delete.setOnClickListener {
                if (onDeleteClick != null) {
                    onDeleteClick!!.onClick(traffic, position)
                }
            }

            itemView.liked.setOnClickListener {
                if (onLikeClick != null) {
                    onLikeClick!!.onClick(traffic, position, itemView.liked)
                }
            }


//            itemView.liked.setOnClickListener {
//                if (traffic.liked!!) {
//                    itemView.liked.setImageResource(R.drawable.ic_heart11)
//                    traffic.liked=false
//                } else {
//                    itemView.liked.setImageResource(R.drawable.ic_heart1)
//                    traffic.liked=true
//                }
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_traffic, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(data!![position], position)
    }

    override fun getItemCount(): Int = data!!.size

    interface OnItemClick {
        fun onClick(traffic: Traffic, position: Int)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    interface OnEditClick {
        fun onClick(traffic: Traffic, position: Int)
    }

    fun setOnEditClick(onEditClick: OnEditClick) {
        this.onEditClick = onEditClick
    }

    interface OnDeleteClick {
        fun onClick(traffic: Traffic, position: Int)
    }

    fun setOnDeleteClick(onDeleteClick: OnDeleteClick) {
        this.onDeleteClick = onDeleteClick
    }

    interface OnLikeClick {
        fun onClick(traffic: Traffic, position: Int, imageView: ImageView)
    }

    fun setOnLikedClick(onLikeClick: OnLikeClick) {
        this.onLikeClick = onLikeClick
    }
}