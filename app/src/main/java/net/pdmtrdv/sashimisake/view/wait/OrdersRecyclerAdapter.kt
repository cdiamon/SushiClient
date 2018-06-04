package net.pdmtrdv.sashimisake.view.wait

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.Model

class OrdersRecyclerAdapter(private var menuDishesList: ArrayList<Model.OrdersResponse>)
    : RecyclerView.Adapter<OrdersRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuDishesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderNumberText.text = menuDishesList[position].id.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderNumberText: TextView = itemView.findViewById(R.id.orderNumberText)
    }
}