package net.pdmtrdv.sashimisake.view.menu

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.MenuCategoryResponse

class CategoryRecyclerAdapter(private var menuCategoriesList: List<MenuCategoryResponse>,
                              private val onCategoryClickCallback: OnCategoryClickCallback)
    : RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuCategoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //names are in pictures
//        holder.menuName.text = menuCategoriesList[position].name
        holder.menuItemLayout.setOnClickListener {
            onCategoryClickCallback.onCategoryClicked(menuCategoriesList[position].dishes)
        }
        Picasso.with(holder.menuImage.context).load(menuCategoriesList[position].image.path).fit().centerCrop().into(holder.menuImage)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuItemLayout: ConstraintLayout = itemView.findViewById(R.id.menuItemLayout)
        val menuImage: ImageView = itemView.findViewById(R.id.menuCatImage)
        val menuName: TextView = itemView.findViewById(R.id.menuCatName)
    }

    interface OnCategoryClickCallback {
        fun onCategoryClicked(dishList: List<MenuCategoryResponse.Dish>)
    }

}