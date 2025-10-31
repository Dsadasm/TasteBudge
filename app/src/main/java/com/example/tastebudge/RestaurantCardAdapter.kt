package com.example.tastebudge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tastebudge.RestaurantListViewAdapter.ViewHolder

class RestaurantCardAdapter(
    private val restaurantList: List<Restaurant>
) : RecyclerView.Adapter<RestaurantCardAdapter.ViewHolder>() {

    // Inflate the layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantCardAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_card, parent, false)
        return ViewHolder(itemView)
    }

    // Set the view
    override fun onBindViewHolder(
        holder: RestaurantCardAdapter.ViewHolder,
        position: Int
    ) {
        val currentItem = restaurantList[position]
        holder.restaurantName.text = currentItem.name
        holder.restaurantPrice.text = currentItem.price
        holder.restaurantRating.rating = currentItem.rating.toFloat()


        // Load image url
        Glide.with(holder.itemView.context)
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
        val restaurantRating: AppCompatRatingBar = itemView.findViewById(R.id.restaurantRating)
    }
}