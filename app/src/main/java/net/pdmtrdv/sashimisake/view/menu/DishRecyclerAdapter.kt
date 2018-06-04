package net.pdmtrdv.sashimisake.view.menu

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.squareup.picasso.Picasso
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.MenuCategoryResponse

class DishRecyclerAdapter(private var menuDishesList: List<MenuCategoryResponse.Dish>,
                          private val onDishClickCallback: OnDishClickCallback)
    : RecyclerView.Adapter<DishRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_dish, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuDishesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.menuName.text = menuDishesList[position].name
        holder.menuDescription.text = menuDishesList[position].ingredients
        holder.menuPrice.text = menuDishesList[position].price.toString().plus("р.")
        holder.addToCartButton.setOnClickListener {
            Toast.makeText(holder.addToCartButton.context, "Добавлено в корзину", LENGTH_SHORT).show()
            onDishClickCallback.onBuyDishClicked(menuDishesList[position])
        }
        Picasso.with(holder.menuImage.context).load(menuDishesList[position].image.path).fit().centerCrop().into(holder.menuImage)
        holder.menuItemLayout.setOnClickListener {
            Toast.makeText(holder.menuItemLayout.context, menuDishesList[position].ingredients, LENGTH_LONG).show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuItemLayout: ConstraintLayout = itemView.findViewById(R.id.menuItemLayout)
        val menuImage: ImageView = itemView.findViewById(R.id.menuImage)
        val menuName: TextView = itemView.findViewById(R.id.menuName)
        val menuDescription: TextView = itemView.findViewById(R.id.menuDescription)
        val menuPrice: TextView = itemView.findViewById(R.id.menuPrice)
        val addToCartButton: ImageView = itemView.findViewById(R.id.addToCartButton)
    }

    interface OnDishClickCallback {
        fun onBuyDishClicked(dish: MenuCategoryResponse.Dish)
    }

}