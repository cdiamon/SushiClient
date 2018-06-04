package net.pdmtrdv.sashimisake.view.kitchen

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.Model
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class KitchenRecyclerAdapter(private var kitchenOrderList: List<Model.OrdersResponse>,
                             private val onKitchenClickCallback: OnKitchenClickCallback) : RecyclerView.Adapter<KitchenRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kitchen, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return kitchenOrderList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 2018-05-26T15:44:33.574
        // 2018-05-31T08:20:21.21

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var testDate: Date? = null
        try {
            testDate = format.parse(kitchenOrderList[position].started)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val formatter = SimpleDateFormat("dd MMM yyyy   hh:mm", Locale.getDefault())
        val newFormat = formatter.format(testDate)
        println(".....Date...$newFormat")

        holder.startTime.text = newFormat
        var dishNames = ""
        kitchenOrderList[position].order_dish.forEach({
            dishNames += (it.dish.name + " ")
        })
        holder.detailsText.text = dishNames
        holder.itemKitchenLayout.setOnLongClickListener {
            onKitchenClickCallback.onKitchenOrderLongClicked(kitchenOrderList[position])
            return@setOnLongClickListener true
        }
        holder.itemKitchenLayout.setOnClickListener {
            onKitchenClickCallback.onKitchenOrderClicked()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemKitchenLayout = itemView.findViewById<ConstraintLayout>(R.id.itemKitchenLayout)
        val startTime = itemView.findViewById<TextView>(R.id.startTime)
        val detailsText = itemView.findViewById<TextView>(R.id.detailsText)
    }

    interface OnKitchenClickCallback {
        fun onKitchenOrderLongClicked(ordersResponse: Model.OrdersResponse)
        fun onKitchenOrderClicked()
    }
}