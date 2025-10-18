package com.example.tastebudge

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

class MyAdapter(
    private var restaurantList: List<Restaurant>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private lateinit var glide: RequestManager

    // Inflate the layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item_layout, parent, false)
        glide = Glide.with(parent.context)
        return ViewHolder(itemView)
    }

    // Set the view
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem = restaurantList[position]
        holder.restaurantName.text = currentItem.name
        holder.restaurantPrice.text = currentItem.price

        // Load image url
        glide
            .load(currentItem.imageUrl)
            .placeholder(R.drawable.question_sign)
            .into(holder.restaurantImage)
    }

    // Return the number of items we want to show
    override fun getItemCount(): Int {
        return restaurantList.size
    }

    // ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantImage: ImageView = itemView.findViewById(R.id.restaurantImage)
        val restaurantName: TextView = itemView.findViewById(R.id.restaurantName)
        val restaurantPrice: TextView = itemView.findViewById(R.id.restaurantPrice)
    }

}
